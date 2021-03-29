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

class TimeZh(
  private val simplified: Boolean,
  private val useWords: Boolean,
  private val useTwentyFourHours: Boolean
) : Time {
  override fun getPercentText(value: Int): String = value.toWords() + '％'

  override fun getDateText(cal: Calendar): String {
    val year = if (useWords) {
      cal.year.toString().toList().joinToString("") { it.toString().toInt().kanji }
    } else {
      cal.year.toString()
    }
    val month = cal.monthNum.toWords()
    val day = cal.day.toWords()
    val weekday = cal.weekday(Locale.CHINESE)
    return "${year}年${month}月${day}日${weekday}"
  }

  override fun getTimeText(cal: Calendar): String {
    val ampm = when {
      useTwentyFourHours -> ""
      cal.hourOfDay == 0 -> "午夜"
      cal.hourOfDay == 12 -> "中午"
      cal.ampm == Calendar.AM -> "上午"
      else -> "下午"
    }
    val hour = (if (useTwentyFourHours) cal.hourOfDay else cal.hour12).toWords(useSpecialKanjiForTwo = true)
    val minute = cal.minute.toWords(useSpecialKanjiForTwo = true)
    val hourSuffix = if (simplified) "点" else "點"
    return "${ampm}${hour}${hourSuffix}${minute}分"
  }

  private fun Int.toWords(useSpecialKanjiForTwo: Boolean = false): String {
    val isSpecialTwo = this == 2 && useSpecialKanjiForTwo
    return when {
      !useWords -> toString()
      isSpecialTwo && simplified -> "两"
      isSpecialTwo -> "兩"
      else -> toWordsCJK(Int::kanji)
    }
  }
}
