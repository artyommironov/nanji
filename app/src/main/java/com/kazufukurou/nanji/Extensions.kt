/*
 * Copyright 2019 Artyom Mironov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kazufukurou.nanji

import android.content.res.Resources
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Resources.dp(dp: Int): Int = ((dp * displayMetrics.density) + 0.5f).toInt()

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
