package com.kazufukurou.nanji.model

import java.util.Calendar
import java.util.Locale

class ChineseTimeSystem(
  private val simplified: Boolean,
  private val verbose: Boolean,
  private val useTwentyFourHours: Boolean
) : TimeSystem {
  override val verboseComponents: Set<DateTimeComponent> = setOf(DateTimeComponent.Date, DateTimeComponent.Time)

  override fun getPercentText(value: Int): String = value.toWords() + '％'

  override fun getDateText(cal: Calendar): String {
    val year = if (verbose) cal.year.toString().kanjiText else cal.year.toString()
    val month = cal.monthNum.toWords()
    val day = cal.day.toWords()
    val weekday = cal.weekday(Locale.CHINESE)
    return "${year}年${month}月${day}日${weekday}"
  }

  override fun getTimeText(cal: Calendar): String {
    val ampm = when {
      useTwentyFourHours -> ""
      cal.hourOfDay == 0 -> "午夜"
      cal.hourOfDay == 12 -> "中午"
      cal.ampm == Calendar.AM -> "上午"
      else -> "下午"
    }
    val hour = (if (useTwentyFourHours) cal.hourOfDay else cal.hour12).toWords(useSpecialKanjiForTwo = true)
    val minute = cal.minute.toWords(useSpecialKanjiForTwo = true)
    val hourSuffix = if (simplified) "点" else "點"
    return "${ampm}${hour}${hourSuffix}${minute}分"
  }

  private fun Int.toWords(useSpecialKanjiForTwo: Boolean = false): String {
    val isSpecialTwo = this == 2 && useSpecialKanjiForTwo
    return when {
      !verbose -> toString()
      isSpecialTwo && simplified -> "两"
      isSpecialTwo -> "兩"
      else -> toWordsCJK { it.kanji }
    }
  }
}
