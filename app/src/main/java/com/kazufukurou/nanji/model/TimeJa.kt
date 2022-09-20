package com.kazufukurou.nanji.model

import java.util.Calendar
import java.util.Locale

class TimeJa(
  private val useEra: Boolean,
  private val useWords: Boolean,
  private val useTwentyFourHours: Boolean
) : Time {
  override fun getPercentText(value: Int): String = value.toWords() + '％'

  override fun getDateText(cal: Calendar): String {
    val (era, eraYearOffset) = if (useEra) "令和" to 2018 else "" to 0
    val year = era + (cal.year - eraYearOffset).toWords()
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

  private fun Int.toWords(): String = if (useWords) toWordsCJK(Int::kanji) else toString()
}
