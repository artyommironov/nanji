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

import com.kazufukurou.nanji.model.TimeCn
import com.kazufukurou.nanji.model.TimeEn
import com.kazufukurou.nanji.model.TimeJa
import com.kazufukurou.nanji.model.TimeKo
import com.kazufukurou.nanji.model.TimeRu
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.test.*

class TimeTest {

  @Test
  fun getTimeTextJa() = with(TimeJa()) {
    assertEquals("午前零時零分", getTimeText(hm(0, 0), false, false, false))
    assertEquals("午前零時一分", getTimeText(hm(0, 1), false, false, false))
    assertEquals("午後零時零分", getTimeText(hm(12, 0), false, false, false))
    assertEquals("午後零時三十四分", getTimeText(hm(12, 34), false, false, false))
    assertEquals("午後一時二十一分", getTimeText(hm(13, 21), false, false, false))
    assertEquals("午前0時0分", getTimeText(hm(0, 0), true, false, false))
    assertEquals("午前0時1分", getTimeText(hm(0, 1), true, false, false))
    assertEquals("午後0時0分", getTimeText(hm(12, 0), true, false, false))
    assertEquals("午後0時34分", getTimeText(hm(12, 34), true, false, false))
    assertEquals("午後1時21分", getTimeText(hm(13, 21), true, false, false))
    assertEquals("零時零分", getTimeText(hm(0, 0), false, true, false))
    assertEquals("零時一分", getTimeText(hm(0, 1), false, true, false))
    assertEquals("十二時零分", getTimeText(hm(12, 0), false, true, false))
    assertEquals("十二時三十四分", getTimeText(hm(12, 34), false, true, false))
    assertEquals("十三時二十一分", getTimeText(hm(13, 21), false, true, false))
    assertEquals("0時0分", getTimeText(hm(0, 0), true, true, false))
    assertEquals("0時1分", getTimeText(hm(0, 1), true, true, false))
    assertEquals("12時0分", getTimeText(hm(12, 0), true, true, false))
    assertEquals("12時34分", getTimeText(hm(12, 34), true, true, false))
    assertEquals("13時21分", getTimeText(hm(13, 21), true, true, false))
  }

  @Test
  fun getTimeTextCn() = with(TimeCn()) {
    assertEquals("午夜十二時零分", getTimeText(hm(0, 0), false, false, false))
    assertEquals("午夜十二時一分", getTimeText(hm(0, 1), false, false, false))
    assertEquals("中午十二時零分", getTimeText(hm(12, 0), false, false, false))
    assertEquals("午夜12時0分", getTimeText(hm(0, 0), true, false, false))
    assertEquals("午夜12時1分", getTimeText(hm(0, 1), true, false, false))
    assertEquals("中午12時0分", getTimeText(hm(12, 0), true, false, false))
    assertEquals("零時零分", getTimeText(hm(0, 0), false, true, false))
    assertEquals("零時一分", getTimeText(hm(0, 1), false, true, false))
    assertEquals("十二時零分", getTimeText(hm(12, 0), false, true, false))
    assertEquals("0時0分", getTimeText(hm(0, 0), true, true, false))
    assertEquals("0時1分", getTimeText(hm(0, 1), true, true, false))
    assertEquals("12時0分", getTimeText(hm(12, 0), true, true, false))
  }

  @Test
  fun getTimeTextKo() = with(TimeKo()) {
    assertEquals("새벽열두시영분", getTimeText(hm(0, 0), false, false, false))
    assertEquals("새벽12시0분", getTimeText(hm(0, 0), true, false, false))
    assertEquals("영시영분", getTimeText(hm(0, 0), false, true, false))
    assertEquals("0시0분", getTimeText(hm(0, 0), true, true, false))
  }

  @Test
  fun getTimeTextRu() = with(TimeRu()) {
    assertEquals("двенадцать часов ноль минут", getTimeText(hm(0, 0), false, false, false))
    assertEquals("двенадцать часов одна минута", getTimeText(hm(0, 1), false, false, false))
    assertEquals("двенадцать часов ноль минут", getTimeText(hm(12, 0), false, false, false))
    assertEquals("двенадцать часов тридцать четыре минуты", getTimeText(hm(12, 34), false, false, false))
    assertEquals("двенадцать часов\nтридцать четыре минуты", getTimeText(hm(12, 34), false, false, true))
    assertEquals("один час двадцать одна минута", getTimeText(hm(13, 21), false, false, false))
    assertEquals("12 часов 0 минут", getTimeText(hm(0, 0), true, false, false))
    assertEquals("12 часов 1 минута", getTimeText(hm(0, 1), true, false, false))
    assertEquals("12 часов 0 минут", getTimeText(hm(12, 0), true, false, false))
    assertEquals("12 часов 34 минуты", getTimeText(hm(12, 34), true, false, false))
    assertEquals("1 час 21 минута", getTimeText(hm(13, 21), true, false, false))
    assertEquals("ноль часов ноль минут", getTimeText(hm(0, 0), false, true, false))
    assertEquals("ноль часов одна минута", getTimeText(hm(0, 1), false, true, false))
    assertEquals("двенадцать часов ноль минут", getTimeText(hm(12, 0), false, true, false))
    assertEquals("двенадцать часов тридцать четыре минуты", getTimeText(hm(12, 34), false, true, false))
    assertEquals("тринадцать часов двадцать одна минута", getTimeText(hm(13, 21), false, true, false))
    assertEquals("0 часов 0 минут", getTimeText(hm(0, 0), true, true, false))
    assertEquals("0 часов 1 минута", getTimeText(hm(0, 1), true, true, false))
    assertEquals("12 часов 0 минут", getTimeText(hm(12, 0), true, true, false))
    assertEquals("12 часов 34 минуты", getTimeText(hm(12, 34), true, true, false))
    assertEquals("13 часов 21 минута", getTimeText(hm(13, 21), true, true, false))
  }

  @Test
  fun getTimeTextEn() = with(TimeEn()) {
    assertEquals("It's twelve o'clock", getTimeText(hm(0, 0), false, false, false))
    assertEquals("It's one minute past twelve", getTimeText(hm(0, 1), false, false, false))
    assertEquals("It's twelve o'clock", getTimeText(hm(12, 0), false, false, false))
    assertEquals("It's twenty six minutes to one", getTimeText(hm(12, 34), false, false, false))
    assertEquals("It's twenty six minutes\nto one", getTimeText(hm(12, 34), false, false, true))
    assertEquals("It's twenty one minutes past one", getTimeText(hm(13, 21), false, false, false))
    assertEquals("It's 12 o'clock", getTimeText(hm(0, 0), true, false, false))
    assertEquals("It's 1 minute past 12", getTimeText(hm(0, 1), true, false, false))
    assertEquals("It's 12 o'clock", getTimeText(hm(12, 0), true, false, false))
    assertEquals("It's 26 minutes to 1", getTimeText(hm(12, 34), true, false, false))
    assertEquals("It's 21 minutes past 1", getTimeText(hm(13, 21), true, false, false))
    assertEquals("zero hundred", getTimeText(hm(0, 0), false, true, false))
    assertEquals("zero zero zero one", getTimeText(hm(0, 1), false, true, false))
    assertEquals("twelve hundred", getTimeText(hm(12, 0), false, true, false))
    assertEquals("twelve thirty four", getTimeText(hm(12, 34), false, true, false))
    assertEquals("thirteen twenty one", getTimeText(hm(13, 21), false, true, false))
    assertEquals("00:00", getTimeText(hm(0, 0), true, true, false))
    assertEquals("00:01", getTimeText(hm(0, 1), true, true, false))
    assertEquals("12:00", getTimeText(hm(12, 0), true, true, false))
    assertEquals("12:34", getTimeText(hm(12, 34), true, true, false))
    assertEquals("13:21", getTimeText(hm(13, 21), true, true, false))
  }

  @Test
  fun getDateText() {
    val date19910418 = ymd(1991, Calendar.APRIL, 18)
    val date20191231 = ymd(2019, Calendar.DECEMBER, 31)

    assertEquals("千九百九十一年四月十八日木曜日", TimeJa().getDateText(date19910418, false, false))
    assertEquals("Thursday, April 18, 1991", TimeEn().getDateText(date19910418, true, false))

    assertEquals("二千十九年十二月三十一日星期二", TimeCn().getDateText(date20191231, false, false))
    assertEquals("二千十九年十二月三十一日火曜日", TimeJa().getDateText(date20191231, false, false))
    assertEquals("令和一年十二月三十一日火曜日", TimeJa().getDateText(date20191231, false, true))
    assertEquals("이천십구년십이월삼십일일화요일", TimeKo().getDateText(date20191231, false, false))
    assertEquals("Tuesday, December 31, 2019", TimeEn().getDateText(date20191231, true, false))
  }

  @Test
  @Ignore
  fun print() {
    val cal = Calendar.getInstance()
    for (i in 0..24 * 60) {
      cal.timeInMillis = i * 60L * 1000L + 60L * 1000L * 60L * 16L
      println(
        listOf(
          listOf(SimpleDateFormat("H:mm").format(cal.time)),
          TimeJa().run { listOf(getTimeText(cal, false, false, false), getTimeText(cal, false, true, false)) },
          TimeCn().run { listOf(getTimeText(cal, false, false, false), getTimeText(cal, false, true, false)) },
          TimeKo().run { listOf(getTimeText(cal, false, false, false), getTimeText(cal, false, true, false)) }
        ).flatten().joinToString()
      )
    }
  }

  fun hm(hour: Int, minute: Int): Calendar = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
  }

  fun ymd(year: Int, month: Int, day: Int) = Calendar.getInstance().apply {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, day)
  }
}
