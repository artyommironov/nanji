package com.kazufukurou.nanji.model

import java.util.Calendar

fun now(
  year: Int? = null,
  month: Int? = null,
  day: Int? = null,
  hour: Int? = null,
  minute: Int? = null,
): Calendar = Calendar.getInstance().apply {
  year?.let { set(Calendar.YEAR, it)   }
  month?.let { set(Calendar.MONTH, it) }
  day?.let { set(Calendar.DAY_OF_MONTH, it) }
  hour?.let { set(Calendar.HOUR_OF_DAY, it) }
  minute?.let { set(Calendar.MINUTE, it) }
}