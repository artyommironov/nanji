package com.kazufukurou.nanji.ui

import androidx.annotation.ColorInt
import com.kazufukurou.nanji.model.DateTimeDisplayMode
import com.kazufukurou.nanji.model.Language
import com.kazufukurou.nanji.model.Prefs
import com.kazufukurou.nanji.model.Time
import com.kazufukurou.nanji.model.TimeEn
import com.kazufukurou.nanji.model.TimeJa
import com.kazufukurou.nanji.model.TimeKo
import com.kazufukurou.nanji.model.TimeRu
import com.kazufukurou.nanji.model.TimeSystem
import com.kazufukurou.nanji.model.TimeZh
import com.kazufukurou.nanji.model.toCodePoints
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class WidgetPresenter(private val prefs: Prefs) {
  fun getState(batteryLevel: Int): State {
    val time = getTime()
    val now = Calendar.getInstance()
    now.timeZone = if (prefs.timeZone.isBlank()) TimeZone.getDefault() else TimeZone.getTimeZone(prefs.timeZone)
    val dateText = time.getDateText(now).transform(useFullWidthCharacters = prefs.fullWidthCharacters)
    val timeText = time.getTimeText(now).transform(useFullWidthCharacters = prefs.fullWidthCharacters)
    val batteryText = if (prefs.showBattery) prefs.batteryLevelPrefix + time.getPercentText(batteryLevel) else ""
    val (header, content) = when (prefs.dateTimeDisplayMode) {
      DateTimeDisplayMode.DateTime -> dateText + batteryText to timeText
      DateTimeDisplayMode.DateOnly -> "" to dateText + batteryText
      DateTimeDisplayMode.TimeOnly -> "" to timeText + batteryText
    }
    return State(
      header = header,
      content = content,
      headerSizeDp = prefs.textSizeRange.first,
      contentSizeDp = prefs.textSize,
      textColor = prefs.textColor,
      bgColor = prefs.bgColor,
      bgCornerRadiusDp = prefs.cornerRadius,
      bgCornerSizeDp = prefs.cornerRadiusRange.last
    )
  }

  private fun getTime(): Time {
    val useWords = prefs.showWords
    val useTwentyFourHours = prefs.twentyFour
    return when (prefs.language) {
      Language.zhCN -> TimeZh(simplified = true, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.zhTW -> TimeZh(simplified = false, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ja -> TimeJa(useEra = prefs.japaneseEra, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ko -> TimeKo(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.en -> TimeEn(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ru -> TimeRu(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.system -> TimeSystem(Locale.getDefault(), useTwentyFourHours = useTwentyFourHours)
    }
  }

  private fun String.transform(useFullWidthCharacters: Boolean): String {
    if (!useFullWidthCharacters) return this
    var result = this
    "0０1１2２3３4４5５6６7７8８9９:："
      .toCodePoints()
      .windowed(2, 2, partialWindows = false, transform = { it[0] to it[1] })
      .forEach { (oldString, newString) -> result = result.replace(oldString, newString) }
    return result
  }

  class State(
    val header: String,
    val content: String,
    val headerSizeDp: Int,
    val contentSizeDp: Int,
    @ColorInt val textColor: Int,
    @ColorInt val bgColor: Int,
    val bgCornerRadiusDp: Int,
    val bgCornerSizeDp: Int,
  )
}