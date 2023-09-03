package com.kazufukurou.nanji.model

import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class KoreanTimeSystemTest {
  @Test
  fun getTimeText() = with(KoreanTimeSystem(verbose = false, useTwentyFourHours = false)) {
    assertEquals("새벽12시0분", getTimeText(hm(0, 0)))
  }

  @Test
  fun getTimeText_Verbose() = with(KoreanTimeSystem(verbose = true, useTwentyFourHours = false)) {
    assertEquals("새벽열두시영분", getTimeText(hm(0, 0)))
  }

  @Test
  fun getTimeText_TwentyFour() = with(KoreanTimeSystem(verbose = false, useTwentyFourHours = true)) {
    assertEquals("0시0분", getTimeText(hm(0, 0)))
  }

  @Test
  fun getTimeText_Verbose_TwentyFour() = with(KoreanTimeSystem(verbose = true, useTwentyFourHours = true)) {
    assertEquals("영시영분", getTimeText(hm(0, 0)))
  }

  @Test
  fun getDateText() = with(KoreanTimeSystem(verbose = true, useTwentyFourHours = false)) {
    assertEquals("천구백구십이년십이월사일금요일", getDateText(ymd(1992, Calendar.DECEMBER, 4)))
    assertEquals("이천십구년십이월삼십일일화요일", getDateText(ymd(2019, Calendar.DECEMBER, 31)))
  }
}