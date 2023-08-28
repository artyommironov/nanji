package com.kazufukurou.nanji.model

import java.util.Calendar

fun TimeSystem.hm(hour: Int, minute: Int): String = getTimeText(
  cal = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
  },
).replace(NBSP, '_')

fun TimeSystem.ymd(year: Int, month: Int, day: Int): String = getDateText(
  cal = Calendar.getInstance().apply {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, day)
  }
)