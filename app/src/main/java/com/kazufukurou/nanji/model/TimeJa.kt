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

class TimeJa : Time {
  override fun getPercentText(value: Int, digits: Boolean): String = convert(value, digits) + '％'

  override fun getDateText(cal: Calendar, digits: Boolean, era: Boolean): String {
    val year = (if (era) "令和" else "") + convert(cal.year - (if (era) 2018 else 0), digits)
    val month = convert(cal.monthNum, digits)
    val day = convert(cal.day, digits)
    val weekday = cal.weekday(Locale.JAPANESE)
    return String.format("%s年%s月%s日%s", year, month, day, weekday)
  }

  override fun getTimeText(cal: Calendar, digits: Boolean, twentyFour: Boolean): String {
    val ampm = when {
      twentyFour -> ""
      cal.ampm == Calendar.AM -> "午前"
      else -> "午後"
    }
    val hour = convert(if (twentyFour) cal.hourOfDay else cal.hour, digits)
    val minute = convert(cal.minute, digits)
    return String.format("%s%s時%s分", ampm, hour, minute)
  }

  private fun convert(num: Int, digits: Boolean): String = if (digits) "$num" else num.toWordsCJK(Int::kanji)
}
