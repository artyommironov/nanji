package com.kazufukurou.nanji.model

import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class EnglishTimeSystemTest {
  @Test
  fun getTimeText() = with(EnglishTimeSystem(useWords = false, useTwentyFourHours = false)) {
    assertEquals("It's 12_o'clock", hm(0, 0))
    assertEquals("It's 1_minute past_12", hm(0, 1))
    assertEquals("It's 12_o'clock", hm(12, 0))
    assertEquals("It's 26_minutes to_1", hm(12, 34))
    assertEquals("It's 21_minutes past_1", hm(13, 21))
  }

  @Test
  fun getTimeText_Words() = with(EnglishTimeSystem(useWords = true, useTwentyFourHours = false)) {
    assertEquals("It's twelve_o'clock", hm(0, 0))
    assertEquals("It's one_minute past_twelve", hm(0, 1))
    assertEquals("It's twelve_o'clock", hm(12, 0))
    assertEquals("It's twenty_six_minutes to_one", hm(12, 34))
    assertEquals("It's twenty_one_minutes past_one", hm(13, 21))
  }

  @Test
  fun getTimeText_TwentyFour() = with(EnglishTimeSystem(useWords = false, useTwentyFourHours = true)) {
    assertEquals("00:00", hm(0, 0))
    assertEquals("00:01", hm(0, 1))
    assertEquals("12:00", hm(12, 0))
    assertEquals("12:34", hm(12, 34))
    assertEquals("13:21", hm(13, 21))
  }

  @Test
  fun getTimeText_Words_TwentyFour() = with(EnglishTimeSystem(useWords = true, useTwentyFourHours = true)) {
    assertEquals("zero hundred", hm(0, 0))
    assertEquals("zero_zero zero_one", hm(0, 1))
    assertEquals("twelve hundred", hm(12, 0))
    assertEquals("twelve thirty_four", hm(12, 34))
    assertEquals("thirteen twenty_one", hm(13, 21))
  }

  @Test
  fun getDateText() = with(EnglishTimeSystem(useWords = false, useTwentyFourHours = false)) {
    assertEquals("Thursday, April 18, 1991", ymd(1991, Calendar.APRIL, 18))
    assertEquals("Tuesday, December 31, 2019", ymd(2019, Calendar.DECEMBER, 31))
  }
}