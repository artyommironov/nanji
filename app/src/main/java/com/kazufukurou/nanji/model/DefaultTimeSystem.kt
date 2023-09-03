package com.kazufukurou.nanji.model

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DefaultTimeSystem(
  private val locale: Locale,
  private val useTwentyFourHours: Boolean
) : TimeSystem {
  override val verboseComponents: Set<DateTimeComponent> = emptySet()

  override fun getPercentText(value: Int): String = "$value%"

  override fun getDateText(cal: Calendar): String {
    return DateFormat.getDateInstance(DateFormat.FULL, locale)
      .apply { timeZone = cal.timeZone }
      .format(cal.time)
  }

  @SuppressLint("SimpleDateFormat")
  override fun getTimeText(cal: Calendar): String {
    return SimpleDateFormat(if (useTwentyFourHours) "H:mm" else "h:mm a")
      .apply { timeZone = cal.timeZone }
      .format(cal.time)
  }
}
