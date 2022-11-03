package com.kazufukurou.nanji.model

import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeTest {
  @Test
  fun getTimeTextJa() = with(TimeJa(useEra = false, useWords = false, useTwentyFourHours = false)) {
    assertEquals("午前0時0分", hm(0, 0))
    assertEquals("午前0時1分", hm(0, 1))
    assertEquals("午後0時0分", hm(12, 0))
    assertEquals("午後0時34分", hm(12, 34))
    assertEquals("午後1時21分", hm(13, 21))
  }

  @Test
  fun getTimeTextJaWords() = with(TimeJa(useEra = false, useWords = true, useTwentyFourHours = false)) {
    assertEquals("午前零時零分", hm(0, 0))
    assertEquals("午前零時一分", hm(0, 1))
    assertEquals("午後零時零分", hm(12, 0))
    assertEquals("午後零時三十四分", hm(12, 34))
    assertEquals("午後一時二十一分", hm(13, 21))
  }

  @Test
  fun getTimeTextJaTwentyFour() = with(TimeJa(useEra = false, useWords = false, useTwentyFourHours = true)) {
    assertEquals("0時0分", hm(0, 0))
    assertEquals("0時1分", hm(0, 1))
    assertEquals("12時0分", hm(12, 0))
    assertEquals("12時34分", hm(12, 34))
    assertEquals("13時21分", hm(13, 21))
  }

  @Test
  fun getTimeTextJaWordsTwentyFour() = with(TimeJa(useEra = false, useWords = true, useTwentyFourHours = true)) {
    assertEquals("零時零分", hm(0, 0))
    assertEquals("零時一分", hm(0, 1))
    assertEquals("十二時零分", hm(12, 0))
    assertEquals("十二時三十四分", hm(12, 34))
    assertEquals("十三時二十一分", hm(13, 21))
  }

  @Test
  fun getTimeTextZh() = with(TimeZh(simplified = true, useWords = false, useTwentyFourHours = false)) {
    assertEquals("午夜12点0分", hm(0, 0))
    assertEquals("午夜12点1分", hm(0, 1))
    assertEquals("中午12点0分", hm(12, 0))
  }

  @Test
  fun getTimeTextZhWords() = with(TimeZh(simplified = true, useWords = true, useTwentyFourHours = false)) {
    assertEquals("午夜十二点零分", hm(0, 0))
    assertEquals("午夜十二点一分", hm(0, 1))
    assertEquals("上午两点两分", hm(2, 2))
    assertEquals("中午十二点零分", hm(12, 0))
    assertEquals("下午两点两分", hm(14, 2))
  }

  @Test
  fun getTimeTextZhTwentyFour() = with(TimeZh(simplified = true, useWords = false, useTwentyFourHours = true)) {
    assertEquals("0点0分", hm(0, 0))
    assertEquals("0点1分", hm(0, 1))
    assertEquals("12点0分", hm(12, 0))
  }

  @Test
  fun getTimeTextZhWordsTwentyFour() = with(TimeZh(simplified = true, useWords = true, useTwentyFourHours = true)) {
    assertEquals("零点零分", hm(0, 0))
    assertEquals("零点一分", hm(0, 1))
    assertEquals("十二点零分", hm(12, 0))
  }

  @Test
  fun getTimeTextKo() = with(TimeKo(useWords = false, useTwentyFourHours = false)) {
    assertEquals("새벽12시0분", hm(0, 0))
  }

  @Test
  fun getTimeTextKoWords() = with(TimeKo(useWords = true, useTwentyFourHours = false)) {
    assertEquals("새벽열두시영분", hm(0, 0))
  }

  @Test
  fun getTimeTextKoTwentyFour() = with(TimeKo(useWords = false, useTwentyFourHours = true)) {
    assertEquals("0시0분", hm(0, 0))
  }

  @Test
  fun getTimeTextKoWordsTwentyFour() = with(TimeKo(useWords = true, useTwentyFourHours = true)) {
    assertEquals("영시영분", hm(0, 0))
  }

  @Test
  fun getTimeTextRu() = with(TimeRu(useWords = false, useTwentyFourHours = false)) {
    assertEquals("12_часов 0_минут", hm(0, 0))
    assertEquals("12_часов 1_минута", hm(0, 1))
    assertEquals("12_часов 0_минут", hm(12, 0))
    assertEquals("12_часов 34_минуты", hm(12, 34))
    assertEquals("1_час 21_минута", hm(13, 21))
  }

  @Test
  fun getTimeTextRuWords() = with(TimeRu(useWords = true, useTwentyFourHours = false)) {
    assertEquals("двенадцать_часов ноль_минут", hm(0, 0))
    assertEquals("двенадцать_часов одна_минута", hm(0, 1))
    assertEquals("двенадцать_часов ноль_минут", hm(12, 0))
    assertEquals("двенадцать_часов тридцать_четыре_минуты", hm(12, 34))
    assertEquals("один_час двадцать_одна_минута", hm(13, 21))
  }

  @Test
  fun getTimeTextRuTwentyFour() = with(TimeRu(useWords = false, useTwentyFourHours = true)) {
    assertEquals("0_часов 0_минут", hm(0, 0))
    assertEquals("0_часов 1_минута", hm(0, 1))
    assertEquals("12_часов 0_минут", hm(12, 0))
    assertEquals("12_часов 34_минуты", hm(12, 34))
    assertEquals("13_часов 21_минута", hm(13, 21))
  }

  @Test
  fun getTimeTextRuWordsTwentyFour() = with(TimeRu(useWords = true, useTwentyFourHours = true)) {
    assertEquals("ноль_часов ноль_минут", hm(0, 0))
    assertEquals("ноль_часов одна_минута", hm(0, 1))
    assertEquals("двенадцать_часов ноль_минут", hm(12, 0))
    assertEquals("двенадцать_часов тридцать_четыре_минуты", hm(12, 34))
    assertEquals("тринадцать_часов двадцать_одна_минута", hm(13, 21))
  }

  @Test
  fun getTimeTextEn() = with(TimeEn(useWords = false, useTwentyFourHours = false)) {
    assertEquals("It's 12_o'clock", hm(0, 0))
    assertEquals("It's 1_minute past_12", hm(0, 1))
    assertEquals("It's 12_o'clock", hm(12, 0))
    assertEquals("It's 26_minutes to_1", hm(12, 34))
    assertEquals("It's 21_minutes past_1", hm(13, 21))
  }

  @Test
  fun getTimeTextEnWords() = with(TimeEn(useWords = true, useTwentyFourHours = false)) {
    assertEquals("It's twelve_o'clock", hm(0, 0))
    assertEquals("It's one_minute past_twelve", hm(0, 1))
    assertEquals("It's twelve_o'clock", hm(12, 0))
    assertEquals("It's twenty_six_minutes to_one", hm(12, 34))
    assertEquals("It's twenty_one_minutes past_one", hm(13, 21))
  }

  @Test
  fun getTimeTextEnTwentyFour() = with(TimeEn(useWords = false, useTwentyFourHours = true)) {
    assertEquals("00:00", hm(0, 0))
    assertEquals("00:01", hm(0, 1))
    assertEquals("12:00", hm(12, 0))
    assertEquals("12:34", hm(12, 34))
    assertEquals("13:21", hm(13, 21))
  }

  @Test
  fun getTimeTextEnWordsTwentyFour() = with(TimeEn(useWords = true, useTwentyFourHours = true)) {
    assertEquals("zero hundred", hm(0, 0))
    assertEquals("zero_zero zero_one", hm(0, 1))
    assertEquals("twelve hundred", hm(12, 0))
    assertEquals("twelve thirty_four", hm(12, 34))
    assertEquals("thirteen twenty_one", hm(13, 21))
  }

  @Test
  fun getDateTextEn() = with(TimeEn(useWords = false, useTwentyFourHours = false)) {
    assertEquals("Thursday, April 18, 1991", ymd(1991, Calendar.APRIL, 18))
    assertEquals("Tuesday, December 31, 2019", ymd(2019, Calendar.DECEMBER, 31))
  }

  @Test
  fun getDateTextJa() = with(TimeJa(useEra = false, useWords = true, useTwentyFourHours = false)) {
    assertEquals("一九九一年四月十八日木曜日", ymd(1991, Calendar.APRIL, 18))
    assertEquals("二〇一九年十二月三十一日火曜日", ymd(2019, Calendar.DECEMBER, 31))
  }

  @Test
  fun getDateTextJaEra() = with(TimeJa(useEra = true, useWords = true, useTwentyFourHours = false)) {
    assertEquals("令和一年十二月三十一日火曜日", ymd(2019, Calendar.DECEMBER, 31))
    assertEquals("令和四年十二月三十一日土曜日", ymd(2022, Calendar.DECEMBER, 31))
  }

  @Test
  fun getDateTextZh() = with(TimeZh(simplified = true, useWords = false, useTwentyFourHours = false)) {
    assertEquals("2019年12月31日星期二", ymd(2019, Calendar.DECEMBER, 31))
  }

  @Test
  fun getDateTextZhWords() = with(TimeZh(simplified = true, useWords = true, useTwentyFourHours = false)) {
    assertEquals("二〇一九年十二月三十一日星期二", ymd(2019, Calendar.DECEMBER, 31))
  }

  @Test
  fun getDateTextKo() = with(TimeKo(useWords = true, useTwentyFourHours = false)) {
    assertEquals("천구백구십이년십이월사일금요일", ymd(1992, Calendar.DECEMBER, 4))
    assertEquals("이천십구년십이월삼십일일화요일", ymd(2019, Calendar.DECEMBER, 31))
  }

  @Test
  fun print() {
    val cal = Calendar.getInstance()
    for (i in 0..24 * 60) {
      cal.timeInMillis = i * 60L * 1000L + 60L * 1000L * 60L * 16L
      println(
        listOf(
          SimpleDateFormat("H:mm").format(cal.time),
          TimeZh(simplified = true, useWords = true, useTwentyFourHours = false).getTimeText(cal),
          TimeZh(simplified = true, useWords = true, useTwentyFourHours = true).getTimeText(cal),
          TimeJa(useEra = false, useWords = true, useTwentyFourHours = false).getTimeText(cal),
          TimeJa(useEra = false, useWords = true, useTwentyFourHours = true).getTimeText(cal),
          TimeKo(useWords = true, useTwentyFourHours = false).getTimeText(cal),
          TimeKo(useWords = true, useTwentyFourHours = true).getTimeText(cal)
        ).joinToString()
      )
    }
  }

  private fun Time.hm(hour: Int, minute: Int): String {
    return getTimeText(
      cal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
      },
    ).replace(NBSP, '_')
  }

  private fun Time.ymd(year: Int, month: Int, day: Int): String {
    return getDateText(
      cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, day)
      }
    )
  }
}
