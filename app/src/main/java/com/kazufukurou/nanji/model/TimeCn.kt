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

import com.kazufukurou.nanji.ampm
import com.kazufukurou.nanji.day
import com.kazufukurou.nanji.hour12
import com.kazufukurou.nanji.hourOfDay
import com.kazufukurou.nanji.minute
import com.kazufukurou.nanji.monthNum
import com.kazufukurou.nanji.weekday
import com.kazufukurou.nanji.year
import java.util.Calendar
import java.util.Locale

class TimeCn : Time {
  private val numberConverter = CJKNumberToTextConverter(::getWord)

  override fun getPercentText(value: Int, digits: Boolean): String = convert(value, digits) + '％'

  override fun getTimeText(cal: Calendar, digits: Boolean, twentyFour: Boolean, multiLine: Boolean): String {
    val ampm = when {
      twentyFour -> ""
      cal.hourOfDay == 0 -> "午夜"
      cal.hourOfDay == 12 -> "中午"
      cal.ampm == Calendar.AM -> "上午"
      else -> "下午"
    }
    val hour = convert(if (twentyFour) cal.hourOfDay else cal.hour12, digits)
    val minute = convert(cal.minute, digits)
    return String.format("%s%s時%s分", ampm, hour, minute)
  }

  override fun getDateText(cal: Calendar, digits: Boolean, era: Boolean): String {
    val year = convert(cal.year, digits)
    val month = convert(cal.monthNum, digits)
    val day = convert(cal.day, digits)
    val weekday = cal.weekday(Locale.CHINESE)
    return String.format("%s年%s月%s日%s", year, month, day, weekday)
  }

  private fun getWord(num: Int): String = when (num) {
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

  fun convert(num: Int, digits: Boolean): String = if (digits) num.toString() else numberConverter.convert(num)
}
