package com.kazufukurou.nanji.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RussianTimeSystemTest {
  @Test
  fun getTimeText() = with(RussianTimeSystem(useWords = false, useTwentyFourHours = false)) {
    assertEquals("12_часов 0_минут", hm(0, 0))
    assertEquals("12_часов 1_минута", hm(0, 1))
    assertEquals("12_часов 0_минут", hm(12, 0))
    assertEquals("12_часов 34_минуты", hm(12, 34))
    assertEquals("1_час 21_минута", hm(13, 21))
  }

  @Test
  fun getTimeText_Words() = with(RussianTimeSystem(useWords = true, useTwentyFourHours = false)) {
    assertEquals("двенадцать_часов ноль_минут", hm(0, 0))
    assertEquals("двенадцать_часов одна_минута", hm(0, 1))
    assertEquals("двенадцать_часов ноль_минут", hm(12, 0))
    assertEquals("двенадцать_часов тридцать_четыре_минуты", hm(12, 34))
    assertEquals("один_час двадцать_одна_минута", hm(13, 21))
  }

  @Test
  fun getTimeText_TwentyFour() = with(RussianTimeSystem(useWords = false, useTwentyFourHours = true)) {
    assertEquals("0_часов 0_минут", hm(0, 0))
    assertEquals("0_часов 1_минута", hm(0, 1))
    assertEquals("12_часов 0_минут", hm(12, 0))
    assertEquals("12_часов 34_минуты", hm(12, 34))
    assertEquals("13_часов 21_минута", hm(13, 21))
  }

  @Test
  fun getTimeText_Words_TwentyFour() = with(RussianTimeSystem(useWords = true, useTwentyFourHours = true)) {
    assertEquals("ноль_часов ноль_минут", hm(0, 0))
    assertEquals("ноль_часов одна_минута", hm(0, 1))
    assertEquals("двенадцать_часов ноль_минут", hm(12, 0))
    assertEquals("двенадцать_часов тридцать_четыре_минуты", hm(12, 34))
    assertEquals("тринадцать_часов двадцать_одна_минута", hm(13, 21))
  }
}