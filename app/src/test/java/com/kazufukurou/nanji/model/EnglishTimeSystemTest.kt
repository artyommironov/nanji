package com.kazufukurou.nanji.model

import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class EnglishTimeSystemTest {
  @Test
  fun getTimeText() = with(EnglishTimeSystem(verbose = false, twentyFourHours = false)) {
    assertEquals("It's 12${NBSP}o'clock", getTimeText(now(hour = 0, minute = 0)))
    assertEquals("It's 1${NBSP}minute past${NBSP}12", getTimeText(now(hour = 0, minute = 1)))
    assertEquals("It's 12${NBSP}o'clock", getTimeText(now(hour = 12, minute = 0)))
    assertEquals("It's 26${NBSP}minutes to${NBSP}1", getTimeText(now(hour = 12, minute = 34)))
    assertEquals("It's 21${NBSP}minutes past${NBSP}1", getTimeText(now(hour = 13, minute = 21)))
  }

  @Test
  fun getTimeText_Verbose() = with(EnglishTimeSystem(verbose = true, twentyFourHours = false)) {
    assertEquals("It's twelve${NBSP}o'clock", getTimeText(now(hour = 0, minute = 0)))
    assertEquals("It's one${NBSP}minute past${NBSP}twelve", getTimeText(now(hour = 0, minute = 1)))
    assertEquals("It's twelve${NBSP}o'clock", getTimeText(now(hour = 12, minute = 0)))
    assertEquals("It's twenty${NBSP}six${NBSP}minutes to${NBSP}one", getTimeText(now(hour = 12, minute = 34)))
    assertEquals("It's twenty${NBSP}one${NBSP}minutes past${NBSP}one", getTimeText(now(hour = 13, minute = 21)))
  }

  @Test
  fun getTimeText_TwentyFour() = with(EnglishTimeSystem(verbose = false, twentyFourHours = true)) {
    assertEquals("00:00", getTimeText(now(hour = 0, minute = 0)))
    assertEquals("00:01", getTimeText(now(hour = 0, minute = 1)))
    assertEquals("12:00", getTimeText(now(hour = 12, minute = 0)))
    assertEquals("12:34", getTimeText(now(hour = 12, minute = 34)))
    assertEquals("13:21", getTimeText(now(hour = 13, minute = 21)))
  }

  @Test
  fun getTimeText_Verbose_TwentyFour() = with(EnglishTimeSystem(verbose = true, twentyFourHours = true)) {
    assertEquals("zero hundred", getTimeText(now(hour = 0, minute = 0)))
    assertEquals("zero${NBSP}zero zero${NBSP}one", getTimeText(now(hour = 0, minute = 1)))
    assertEquals("twelve hundred", getTimeText(now(hour = 12, minute = 0)))
    assertEquals("twelve thirty${NBSP}four", getTimeText(now(hour = 12, minute = 34)))
    assertEquals("thirteen twenty${NBSP}one", getTimeText(now(hour = 13, minute = 21)))
  }

  @Test
  fun getDateText() = with(EnglishTimeSystem(verbose = false, twentyFourHours = false)) {
    assertEquals("Thursday, April 18, 1991", getDateText(now(1991, Calendar.APRIL, 18)))
    assertEquals("Tuesday, December 31, 2019", getDateText(now(2019, Calendar.DECEMBER, 31)))
  }
}