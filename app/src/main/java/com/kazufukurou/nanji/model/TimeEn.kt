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
  private val grammarSystem = TimeSystem(Locale.ENGLISH)
  private val numberConverter = EnRuNumberToTextConverter(::getWord)

  override fun getPercentText(value: Int, digits: Boolean): String = grammarSystem.getPercentText(value, digits)

  override fun getDateText(cal: Calendar, digits: Boolean, era: Boolean): String {
    return grammarSystem.getDateText(cal, digits, era)
  }

  override fun getTimeText(cal: Calendar, digits: Boolean, twentyFour: Boolean, multiLine: Boolean): String {
    val h = cal.hourOfDay
    val m = cal.minute
    return when {
      twentyFour && digits -> "$h".padStart(2, '0') + ":" + "$m".padStart(2, '0')
      twentyFour -> {
        val zero = getWord(0)
        val hundred = getWord(100)
        val hour = convert(h, digits)
        val minute = convert(m, digits)
        val hourResult = if (h < 10 && !(h == 0 && m == 0)) "$zero $hour" else hour
        val minuteResult = if (m == 0) hundred else if (m < 10) "$zero $minute" else minute
        "$hourResult $minuteResult"
      }
      else -> {
        val hourValue = (cal.hour12 + (if (m > 30) 1 else 0)).takeIf { it < 13 } ?: 1
        val hour = convert(hourValue, digits)
        val hourPrefix = if (m > 30) "to " else "past "
        val minuteValue = if (m > 30) 60 - m else m
        val minute = convert(minuteValue, digits)
        val divider = (if (multiLine) "\n" else " ")
        "It's " + when (minuteValue) {
          0 -> "$hour o'clock"
          1 -> "$minute minute$divider$hourPrefix$hour"
          15 -> "quarter$divider$hourPrefix$hour"
          30 -> "half$divider$hourPrefix$hour"
          else -> "$minute minutes$divider$hourPrefix$hour"
        }
      }
    }
  }

  private fun getWord(digit: Int) = when (digit) {
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

  private fun convert(num: Int, digits: Boolean): String = if (digits) num.toString() else numberConverter.convert(num)
}
