package com.kazufukurou.nanji.model

import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class ChineseTimeSystemTest {
  @Test
  fun getTimeText() = with(ChineseTimeSystem(simplified = true, useWords = false, useTwentyFourHours = false)) {
    assertEquals("午夜12点0分", hm(0, 0))
    assertEquals("午夜12点1分", hm(0, 1))
    assertEquals("中午12点0分", hm(12, 0))
  }

  @Test
  fun getTimeText_Words() = with(ChineseTimeSystem(simplified = true, useWords = true, useTwentyFourHours = false)) {
    assertEquals("午夜十二点零分", hm(0, 0))
    assertEquals("午夜十二点一分", hm(0, 1))
    assertEquals("上午两点两分", hm(2, 2))
    assertEquals("中午十二点零分", hm(12, 0))
    assertEquals("下午两点两分", hm(14, 2))
  }

  @Test
  fun getTimeText_TwentyFour() = with(
    ChineseTimeSystem(simplified = true, useWords = false, useTwentyFourHours = true)
  ) {
    assertEquals("0点0分", hm(0, 0))
    assertEquals("0点1分", hm(0, 1))
    assertEquals("12点0分", hm(12, 0))
  }

  @Test
  fun getTimeText_Words_TwentyFour() = with(
    ChineseTimeSystem(simplified = true, useWords = true, useTwentyFourHours = true)
  ) {
    assertEquals("零点零分", hm(0, 0))
    assertEquals("零点一分", hm(0, 1))
    assertEquals("十二点零分", hm(12, 0))
  }


  @Test
  fun getDateText() = with(ChineseTimeSystem(simplified = true, useWords = false, useTwentyFourHours = false)) {
    assertEquals("2019年12月31日星期二", ymd(2019, Calendar.DECEMBER, 31))
  }

  @Test
  fun getDateText_Words() = with(ChineseTimeSystem(simplified = true, useWords = true, useTwentyFourHours = false)) {
    assertEquals("二〇一九年十二月三十一日星期二", ymd(2019, Calendar.DECEMBER, 31))
  }
}