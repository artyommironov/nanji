/*
 * Copyright 2021 Artyom Mironov
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

package com.kazufukurou.nanji.model

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val NBSP = '\u00a0'

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

fun Int.toWordsEnRu(wordSource: (Int) -> String): String {
  val num = this
  return with(StringBuilder()) {
    when (num) {
      in 0..20, 30, 40, 50, 60, 70, 80, 90, 100 -> append(wordSource(num))
      in 21..99 -> append(wordSource(num - num % 10)).append(NBSP).append(wordSource(num % 10))
    }
    toString()
  }
}

fun Int.toWordsCJK(wordSource: (Int) -> String): String {
  val num = this
  return buildString {
    when (num) {
      in 0..10 -> append(wordSource(num))
      in 11..19 -> append(wordSource(10)).append((num - 10).toWordsCJK(wordSource))
      in 20..99 -> append(wordSource(num / 10)).append((num - 10 * (num / 10 - 1)).toWordsCJK(wordSource))
      100 -> append(wordSource(100))
      in 101..199 -> append(wordSource(100)).append((num - 100).toWordsCJK(wordSource))
      in 200..999 -> append(wordSource(num / 100)).append((num - 100 * (num / 100 - 1)).toWordsCJK(wordSource))
      1000 -> append(wordSource(1000))
      in 1001..1999 -> append(wordSource(1000)).append((num - 1000).toWordsCJK(wordSource))
      in 2000..9999 -> append(wordSource(num / 1000)).append((num - 1000 * (num / 1000 - 1)).toWordsCJK(wordSource))
      else -> Unit
    }
  }
}

val Int.kanji: String get() = when (this) {
  0 -> "零"
  1 -> "一"
  2 -> "二"
  3 -> "三"
  4 -> "四"
  5 -> "五"
  6 -> "六"
  7 -> "七"
  8 -> "八"
  9 -> "九"
  10 -> "十"
  100 -> "百"
  1000 -> "千"
  else -> ""
}

