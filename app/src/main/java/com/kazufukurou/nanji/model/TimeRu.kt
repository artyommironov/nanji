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

import java.util.Calendar
import java.util.Locale

class TimeRu : Time {
  private val timeSystem = TimeSystem(Locale("ru"))

  override fun getPercentText(value: Int, digits: Boolean): String = timeSystem.getPercentText(value, digits)

  override fun getDateText(cal: Calendar, digits: Boolean, era: Boolean): String {
    return timeSystem.getDateText(cal, digits, era)
  }

  override fun getTimeText(cal: Calendar, digits: Boolean, twentyFour: Boolean): String {
    val (h, m) = cal.run { (if (twentyFour) cal.hourOfDay else cal.hour12) to minute }
    val hour = convert(h, female = false, digits = digits) + NBSP + getPlural(h, "час", "часа", "часов")
    val minute = convert(m, female = true, digits = digits) + NBSP + getPlural(m, "минута", "минуты", "минут")
    return "$hour $minute"
  }

  private fun Int.toWord(female: Boolean) = when (this) {
    0 -> "ноль"
    1 -> if (female) "одна" else "один"
    2 -> if (female) "две" else "два"
    3 -> "три"
    4 -> "четыре"
    5 -> "пять"
    6 -> "шесть"
    7 -> "семь"
    8 -> "восемь"
    9 -> "девять"
    10 -> "десять"
    11 -> "одиннадцать"
    12 -> "двенадцать"
    13 -> "тринадцать"
    14 -> "четырнадцать"
    15 -> "пятнадцать"
    16 -> "шестнадцать"
    17 -> "семнадцать"
    18 -> "восемнадцать"
    19 -> "девятнадцать"
    20 -> "двадцать"
    30 -> "тридцать"
    40 -> "сорок"
    50 -> "пятьдесят"
    else -> ""
  }

  private fun getPlural(num: Int, formOne: String, formTwo: String, formFive: String): String {
    val n10 = num % 10
    val n100 = num % 100
    return when {
      n10 == 1 && n100 != 11 -> formOne
      n10 in 2..4 && n100 !in 10..19 -> formTwo
      else -> formFive
    }
  }

  private fun convert(num: Int, female: Boolean, digits: Boolean): String {
    return when {
      digits -> num.toString()
      female -> num.toWordsEnRu { it.toWord(female = true) }
      else -> num.toWordsEnRu { it.toWord(female = false) }
    }
  }
}
