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

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimeSystem(private val locale: Locale) : Time {
  override fun getPercentText(value: Int, digits: Boolean): String = "$value%"

  override fun getDateText(cal: Calendar, digits: Boolean, era: Boolean): String {
    return DateFormat.getDateInstance(DateFormat.FULL, locale)
      .apply { timeZone = cal.timeZone }
      .format(cal.time)
  }

  override fun getTimeText(cal: Calendar, digits: Boolean, twentyFour: Boolean, multiLine: Boolean): String {
    return SimpleDateFormat(if (twentyFour) "H:mm" else "h:mm a")
      .apply { timeZone = cal.timeZone }
      .format(cal.time)
  }
}
