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

package com.kazufukurou.nanji.model

import java.util.Calendar
import java.util.Locale

class TimeEn : Time {
  private val timeSystem = TimeSystem(Locale.ENGLISH)

  override fun getPercentText(value: Int, digits: Boolean): String = timeSystem.getPercentText(value, digits)

  override fun getDateText(cal: Calendar, digits: Boolean, era: Boolean): String {
    return timeSystem.getDateText(cal, digits, era)
  }

  override fun getTimeText(cal: Calendar, digits: Boolean, twentyFour: Boolean): String {
    val (h, m) = cal.run { hourOfDay to minute }
    return when {
      twentyFour && digits -> "$h".padStart(2, '0') + ":" + "$m".padStart(2, '0')
      twentyFour -> {
        val hour = convert(h, digits)
          .let { if (h < 10 && !(h == 0 && m == 0)) "${0.word}$NBSP$it" else it }
        val minute = convert(m, digits)
          .let { if (m == 0) 100.word else if (m < 10) "${0.word}$NBSP$it" else it }
        "$hour $minute"
      }
      else -> {
        val (hr, mr) = when {
          m > 30 -> ((cal.hour12 + 1).takeIf { it < 13 } ?: 1) to (60 - m)
          else -> cal.hour12 to m
        }
        val minute = convert(mr, digits) + NBSP + (if (mr == 1) "minute" else "minutes")
        val hour = convert(hr, digits)
        val prefix = if (m > 30) "to" else "past"
        "It's " + when (mr) {
          0 -> "$hour${NBSP}o'clock"
          15 -> "quarter $prefix$NBSP$hour"
          30 -> "half $prefix$NBSP$hour"
          else -> "$minute $prefix$NBSP$hour"
        }
      }
    }
  }

  private val Int.word: String get() = when (this) {
    0 -> "zero"
    1 -> "one"
    2 -> "two"
    3 -> "three"
    4 -> "four"
    5 -> "five"
    6 -> "six"
    7 -> "seven"
    8 -> "eight"
    9 -> "nine"
    10 -> "ten"
    11 -> "eleven"
    12 -> "twelve"
    13 -> "thirteen"
    14 -> "fourteen"
    15 -> "fifteen"
    16 -> "sixteen"
    17 -> "seventeen"
    18 -> "eighteen"
    19 -> "nineteen"
    20 -> "twenty"
    30 -> "thirty"
    40 -> "forty"
    50 -> "fifty"
    100 -> "hundred"
    else -> ""
  }

  private fun convert(num: Int, digits: Boolean): String = if (digits) "$num" else num.toWordsEnRu { it.word }
}
