package com.kazufukurou.nanji.model

import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class JapaneseTimeSystemTest {
  @Test
  fun getTimeText() = with(JapaneseTimeSystem(useEra = false, verbose = false, useTwentyFourHours = false)) {
    assertEquals("午前0時0分", getTimeText(hm(0, 0)))
    assertEquals("午前0時1分", getTimeText(hm(0, 1)))
    assertEquals("午後0時0分", getTimeText(hm(12, 0)))
    assertEquals("午後0時34分", getTimeText(hm(12, 34)))
    assertEquals("午後1時21分", getTimeText(hm(13, 21)))
  }

  @Test
  fun getTimeText_Verbose() = with(JapaneseTimeSystem(useEra = false, verbose = true, useTwentyFourHours = false)) {
    assertEquals("午前零時零分", getTimeText(hm(0, 0)))
    assertEquals("午前零時一分", getTimeText(hm(0, 1)))
    assertEquals("午後零時零分", getTimeText(hm(12, 0)))
    assertEquals("午後零時三十四分", getTimeText(hm(12, 34)))
    assertEquals("午後一時二十一分", getTimeText(hm(13, 21)))
  }

  @Test
  fun getTimeText_TwentyFour() = with(JapaneseTimeSystem(useEra = false, verbose = false, useTwentyFourHours = true)) {
    assertEquals("0時0分", getTimeText(hm(0, 0)))
    assertEquals("0時1分", getTimeText(hm(0, 1)))
    assertEquals("12時0分", getTimeText(hm(12, 0)))
    assertEquals("12時34分", getTimeText(hm(12, 34)))
    assertEquals("13時21分", getTimeText(hm(13, 21)))
  }

  @Test
  fun getTimeText_Verbose_TwentyFour() = with(
    JapaneseTimeSystem(useEra = false, verbose = true, useTwentyFourHours = true)
  ) {
    assertEquals("零時零分", getTimeText(hm(0, 0)))
    assertEquals("零時一分", getTimeText(hm(0, 1)))
    assertEquals("十二時零分", getTimeText(hm(12, 0)))
    assertEquals("十二時三十四分", getTimeText(hm(12, 34)))
    assertEquals("十三時二十一分", getTimeText(hm(13, 21)))
  }

  @Test
  fun getDateText() = with(JapaneseTimeSystem(useEra = false, verbose = true, useTwentyFourHours = false)) {
    assertEquals("一九九一年四月十八日木曜日", getDateText(ymd(1991, Calendar.APRIL, 18)))
    assertEquals("二〇一九年十二月三十一日火曜日", getDateText(ymd(2019, Calendar.DECEMBER, 31)))
  }

  @Test
  fun getDateText_Era() = with(JapaneseTimeSystem(useEra = true, verbose = true, useTwentyFourHours = false)) {
    assertEquals("令和一年十二月三十一日火曜日", getDateText(ymd(2019, Calendar.DECEMBER, 31)))
    assertEquals("令和四年十二月三十一日土曜日", getDateText(ymd(2022, Calendar.DECEMBER, 31)))
  }
}