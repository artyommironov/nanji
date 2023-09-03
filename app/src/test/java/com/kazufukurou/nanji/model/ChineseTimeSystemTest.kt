package com.kazufukurou.nanji.model

import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class ChineseTimeSystemTest {
  @Test
  fun getTimeText() = with(ChineseTimeSystem(simplified = true, verbose = false, twentyFourHours = false)) {
    assertEquals("午夜12点0分", getTimeText(now(hour = 0, minute = 0)))
    assertEquals("午夜12点1分", getTimeText(now(hour = 0, minute = 1)))
    assertEquals("中午12点0分", getTimeText(now(hour = 12, minute = 0)))
  }

  @Test
  fun getTimeText_Verbose() = with(ChineseTimeSystem(simplified = true, verbose = true, twentyFourHours = false)) {
    assertEquals("午夜十二点零分", getTimeText(now(hour = 0, minute = 0)))
    assertEquals("午夜十二点一分", getTimeText(now(hour = 0, minute = 1)))
    assertEquals("上午两点两分", getTimeText(now(hour = 2, minute = 2)))
    assertEquals("中午十二点零分", getTimeText(now(hour = 12, minute = 0)))
    assertEquals("下午两点两分", getTimeText(now(hour = 14, minute = 2)))
  }

  @Test
  fun getTimeText_TwentyFour() = with(
    ChineseTimeSystem(simplified = true, verbose = false, twentyFourHours = true)
  ) {
    assertEquals("0点0分", getTimeText(now(hour = 0, minute = 0)))
    assertEquals("0点1分", getTimeText(now(hour = 0, minute = 1)))
    assertEquals("12点0分", getTimeText(now(hour = 12, minute = 0)))
  }

  @Test
  fun getTimeText_Verbose_TwentyFour() = with(
    ChineseTimeSystem(simplified = true, verbose = true, twentyFourHours = true)
  ) {
    assertEquals("零点零分", getTimeText(now(hour = 0, minute = 0)))
    assertEquals("零点一分", getTimeText(now(hour = 0, minute = 1)))
    assertEquals("十二点零分", getTimeText(now(hour = 12, minute = 0)))
  }


  @Test
  fun getDateText() = with(ChineseTimeSystem(simplified = true, verbose = false, twentyFourHours = false)) {
    assertEquals("2019年12月31日星期二", getDateText(now(2019, Calendar.DECEMBER, 31)))
  }

  @Test
  fun getDateText_Verbose() = with(ChineseTimeSystem(simplified = true, verbose = true, twentyFourHours = false)) {
    assertEquals("二〇一九年十二月三十一日星期二", getDateText(now(2019, Calendar.DECEMBER, 31)))
  }
}