package com.kazufukurou.nanji.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimeSystem(
  private val locale: Locale,
  private val useTwentyFourHours: Boolean
) : Time {
  override fun getPercentText(value: Int): String = "$value%"

  override fun getDateText(cal: Calendar): String {
    return DateFormat.getDateInstance(DateFormat.FULL, locale)
      .apply { timeZone = cal.timeZone }
      .format(cal.time)
  }

  override fun getTimeText(cal: Calendar): String {
    return SimpleDateFormat(if (useTwentyFourHours) "H:mm" else "h:mm a")
      .apply { timeZone = cal.timeZone }
      .format(cal.time)
  }
}
