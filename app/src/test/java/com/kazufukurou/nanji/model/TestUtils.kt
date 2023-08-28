package com.kazufukurou.nanji.model

import java.util.Calendar

fun hm(hour: Int, minute: Int): Calendar = Calendar.getInstance().apply {
  set(Calendar.HOUR_OF_DAY, hour)
  set(Calendar.MINUTE, minute)
}

fun ymd(year: Int, month: Int, day: Int): Calendar = Calendar.getInstance().apply {
  set(Calendar.YEAR, year)
  set(Calendar.MONTH, month)
  set(Calendar.DAY_OF_MONTH, day)
}