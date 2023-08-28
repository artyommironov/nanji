package com.kazufukurou.nanji.model

import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeSystemTest {
  @Test
  fun print() {
    val cal = Calendar.getInstance()
    for (i in 0..24 * 60) {
      cal.timeInMillis = i * 60L * 1000L + 60L * 1000L * 60L * 16L
      println(
        listOf(
          SimpleDateFormat("H:mm").format(cal.time),
          ChineseTimeSystem(simplified = true, useWords = true, useTwentyFourHours = false).getTimeText(cal),
          ChineseTimeSystem(simplified = true, useWords = true, useTwentyFourHours = true).getTimeText(cal),
          JapaneseTimeSystem(useEra = false, useWords = true, useTwentyFourHours = false).getTimeText(cal),
          JapaneseTimeSystem(useEra = false, useWords = true, useTwentyFourHours = true).getTimeText(cal),
          KoreanTimeSystem(useWords = true, useTwentyFourHours = false).getTimeText(cal),
          KoreanTimeSystem(useWords = true, useTwentyFourHours = true).getTimeText(cal)
        ).joinToString()
      )
    }
  }
}
