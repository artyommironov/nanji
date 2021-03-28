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

import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeTest {
  @Test
  fun getTimeTextJa() = with(TimeJa()) {
    assertEquals("午前零時零分", hm(0, 0))
    assertEquals("午前零時一分", hm(0, 1))
    assertEquals("午後零時零分", hm(12, 0))
    assertEquals("午後零時三十四分", hm(12, 34))
    assertEquals("午後一時二十一分", hm(13, 21))
    assertEquals("午前0時0分", hm(0, 0, digits = true))
    assertEquals("午前0時1分", hm(0, 1, digits = true))
    assertEquals("午後0時0分", hm(12, 0, digits = true))
    assertEquals("午後0時34分", hm(12, 34, digits = true))
    assertEquals("午後1時21分", hm(13, 21, digits = true))
    assertEquals("零時零分", hm(0, 0, twentyFour = true))
    assertEquals("零時一分", hm(0, 1, twentyFour = true))
    assertEquals("十二時零分", hm(12, 0, twentyFour = true))
    assertEquals("十二時三十四分", hm(12, 34, twentyFour = true))
    assertEquals("十三時二十一分", hm(13, 21, twentyFour = true))
    assertEquals("0時0分", hm(0, 0, digits = true, twentyFour = true))
    assertEquals("0時1分", hm(0, 1, digits = true, twentyFour = true))
    assertEquals("12時0分", hm(12, 0, digits = true, twentyFour = true))
    assertEquals("12時34分", hm(12, 34, digits = true, twentyFour = true))
    assertEquals("13時21分", hm(13, 21, digits = true, twentyFour = true))
  }

  @Test
  fun getTimeTextCn() = with(TimeZh(simplified = true)) {
    assertEquals("午夜十二点零分", hm(0, 0))
    assertEquals("午夜十二点一分", hm(0, 1))
    assertEquals("上午两点两分", hm(2, 2))
    assertEquals("中午十二点零分", hm(12, 0))
    assertEquals("下午两点两分", hm(14, 2))
    assertEquals("午夜12点0分", hm(0, 0, digits = true))
    assertEquals("午夜12点1分", hm(0, 1, digits = true))
    assertEquals("中午12点0分", hm(12, 0, digits = true))
    assertEquals("零点零分", hm(0, 0, twentyFour = true))
    assertEquals("零点一分", hm(0, 1, twentyFour = true))
    assertEquals("十二点零分", hm(12, 0, twentyFour = true))
    assertEquals("0点0分", hm(0, 0, digits = true, twentyFour = true))
    assertEquals("0点1分", hm(0, 1, digits = true, twentyFour = true))
    assertEquals("12点0分", hm(12, 0, digits = true, twentyFour = true))
  }

  @Test
  fun getTimeTextKo() = with(TimeKo()) {
    assertEquals("새벽열두시영분", hm(0, 0))
    assertEquals("새벽12시0분", hm(0, 0, digits = true))
    assertEquals("영시영분", hm(0, 0, twentyFour = true))
    assertEquals("0시0분", hm(0, 0, digits = true, twentyFour = true))
  }

  @Test
  fun getTimeTextRu() = with(TimeRu()) {
    assertEquals("двенадцать_часов ноль_минут", hm(0, 0))
    assertEquals("двенадцать_часов одна_минута", hm(0, 1))
    assertEquals("двенадцать_часов ноль_минут", hm(12, 0))
    assertEquals("двенадцать_часов тридцать_четыре_минуты", hm(12, 34))
    assertEquals("один_час двадцать_одна_минута", hm(13, 21))
    assertEquals("12_часов 0_минут", hm(0, 0, digits = true))
    assertEquals("12_часов 1_минута", hm(0, 1, digits = true))
    assertEquals("12_часов 0_минут", hm(12, 0, digits = true))
    assertEquals("12_часов 34_минуты", hm(12, 34, digits = true))
    assertEquals("1_час 21_минута", hm(13, 21, digits = true))
    assertEquals("ноль_часов ноль_минут", hm(0, 0, twentyFour = true))
    assertEquals("ноль_часов одна_минута", hm(0, 1, twentyFour = true))
    assertEquals("двенадцать_часов ноль_минут", hm(12, 0, twentyFour = true))
    assertEquals("двенадцать_часов тридцать_четыре_минуты", hm(12, 34, twentyFour = true))
    assertEquals("тринадцать_часов двадцать_одна_минута", hm(13, 21, twentyFour = true))
    assertEquals("0_часов 0_минут", hm(0, 0, digits = true, twentyFour = true))
    assertEquals("0_часов 1_минута", hm(0, 1, digits = true, twentyFour = true))
    assertEquals("12_часов 0_минут", hm(12, 0, digits = true, twentyFour = true))
    assertEquals("12_часов 34_минуты", hm(12, 34, digits = true, twentyFour = true))
    assertEquals("13_часов 21_минута", hm(13, 21, digits = true, twentyFour = true))
  }

  @Test
  fun getTimeTextEn() = with(TimeEn()) {
    assertEquals("It's twelve_o'clock", hm(0, 0))
    assertEquals("It's one_minute past_twelve", hm(0, 1))
    assertEquals("It's twelve_o'clock", hm(12, 0))
    assertEquals("It's twenty_six_minutes to_one", hm(12, 34))
    assertEquals("It's twenty_one_minutes past_one", hm(13, 21))
    assertEquals("It's 12_o'clock", hm(0, 0, digits = true))
    assertEquals("It's 1_minute past_12", hm(0, 1, digits = true))
    assertEquals("It's 12_o'clock", hm(12, 0, digits = true))
    assertEquals("It's 26_minutes to_1", hm(12, 34, digits = true))
    assertEquals("It's 21_minutes past_1", hm(13, 21, digits = true))
    assertEquals("zero hundred", hm(0, 0, twentyFour = true))
    assertEquals("zero_zero zero_one", hm(0, 1, twentyFour = true))
    assertEquals("twelve hundred", hm(12, 0, twentyFour = true))
    assertEquals("twelve thirty_four", hm(12, 34, twentyFour = true))
    assertEquals("thirteen twenty_one", hm(13, 21, twentyFour = true))
    assertEquals("00:00", hm(0, 0, digits = true, twentyFour = true))
    assertEquals("00:01", hm(0, 1, digits = true, twentyFour = true))
    assertEquals("12:00", hm(12, 0, digits = true, twentyFour = true))
    assertEquals("12:34", hm(12, 34, digits = true, twentyFour = true))
    assertEquals("13:21", hm(13, 21, digits = true, twentyFour = true))
  }

  @Test
  fun getDateTextAll() {
    assertEquals("Thursday, April 18, 1991", TimeEn().ymd(1991, Calendar.APRIL, 18, digits = true))
    assertEquals("Tuesday, December 31, 2019", TimeEn().ymd(2019, Calendar.DECEMBER, 31, digits = true))
    assertEquals("千九百九十一年四月十八日木曜日", TimeJa().ymd(1991, Calendar.APRIL, 18))
    assertEquals("二千十九年十二月三十一日火曜日", TimeJa().ymd(2019, Calendar.DECEMBER, 31))
    assertEquals("令和一年十二月三十一日火曜日", TimeJa().ymd(2019, Calendar.DECEMBER, 31, era = true))
    assertEquals("二千十九年十二月三十一日星期二", TimeZh(simplified = true).ymd(2019, Calendar.DECEMBER, 31))
    assertEquals("이천십구년십이월삼십일일화요일", TimeKo().ymd(2019, Calendar.DECEMBER, 31))
  }

  @Test
  fun print() {
    val cal = Calendar.getInstance()
    for (i in 0..24 * 60) {
      cal.timeInMillis = i * 60L * 1000L + 60L * 1000L * 60L * 16L
      println(
        listOf(
          SimpleDateFormat("H:mm").format(cal.time),
          TimeZh(simplified = true).getTimeText(cal, digits = false, twentyFour = false),
          TimeZh(simplified = true).getTimeText(cal, digits = false, twentyFour = true),
          TimeJa().getTimeText(cal, digits = false, twentyFour = false),
          TimeJa().getTimeText(cal, digits = false, twentyFour = true),
          TimeKo().getTimeText(cal, digits = false, twentyFour = false),
          TimeKo().getTimeText(cal, digits = false, twentyFour = true)
        ).joinToString()
      )
    }
  }

  private fun Time.hm(hour: Int, minute: Int, digits: Boolean = false, twentyFour: Boolean = false): String {
    return getTimeText(
      cal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
      },
      digits = digits,
      twentyFour = twentyFour
    ).replace(NBSP, '_')
  }

  private fun Time.ymd(year: Int, month: Int, day: Int, digits: Boolean = false, era: Boolean = false): String {
    return getDateText(
      cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, day)
      },
      digits = digits,
      era = era
    )
  }
}
