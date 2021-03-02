package com.kazufukurou.nanji.model

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun String.toCodePoints(): List<String> {
  var offset = 0
  val result = mutableListOf<String>()
  while (offset < length) {
    val codePoint = codePointAt(offset)
    result.add(StringBuilder().appendCodePoint(codePoint).toString())
    offset += Character.charCount(codePoint)
  }
  return result
}

val Calendar.year: Int get() = get(Calendar.YEAR)
val Calendar.monthNum: Int get() = get(Calendar.MONTH) + 1
val Calendar.day: Int get() = get(Calendar.DAY_OF_MONTH)
val Calendar.ampm: Int get() = get(Calendar.AM_PM)
val Calendar.hour: Int get() = get(Calendar.HOUR)
val Calendar.hourOfDay: Int get() = get(Calendar.HOUR_OF_DAY)
val Calendar.hour12: Int get() = hour.let { if (it == 0) 12 else it }
val Calendar.minute: Int get() = get(Calendar.MINUTE)
fun Calendar.weekday(locale: Locale): String = SimpleDateFormat("EEEE", DateFormatSymbols(locale)).format(time)
