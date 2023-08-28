package com.kazufukurou.nanji.model

import java.util.Calendar
import java.util.Locale

class JapaneseTimeSystem(
  private val useEra: Boolean,
  private val useWords: Boolean,
  private val useTwentyFourHours: Boolean
) : TimeSystem {
  override fun getPercentText(value: Int): String = value.toWords() + '％'

  override fun getDateText(cal: Calendar): String {
    val year = when {
      useEra -> "令和" + (cal.year - 2018).toWords()
      useWords -> cal.year.toString().kanjiText
      else -> cal.year.toString()
    }
    val month = cal.monthNum.toWords()
    val day = cal.day.toWords()
    val weekday = cal.weekday(Locale.JAPANESE)
    return "${year}年${month}月${day}日${weekday}"
  }

  override fun getTimeText(cal: Calendar): String {
    val ampm = when {
      useTwentyFourHours -> ""
      cal.ampm == Calendar.AM -> "午前"
      else -> "午後"
    }
    val hour = (if (useTwentyFourHours) cal.hourOfDay else cal.hour).toWords()
    val minute = cal.minute.toWords()
    return "${ampm}${hour}時${minute}分"
  }

  private fun Int.toWords(): String = if (useWords) toWordsCJK { it.kanji } else toString()
}
