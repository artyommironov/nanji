package com.kazufukurou.nanji.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RussianTimeSystemTest {
  @Test
  fun getTimeText() = with(RussianTimeSystem(verbose = false, twentyFourHours = false)) {
    assertEquals("12${NBSP}часов 0${NBSP}минут", getTimeText(hm(0, 0)))
    assertEquals("12${NBSP}часов 1${NBSP}минута", getTimeText(hm(0, 1)))
    assertEquals("12${NBSP}часов 0${NBSP}минут", getTimeText(hm(12, 0)))
    assertEquals("12${NBSP}часов 34${NBSP}минуты", getTimeText(hm(12, 34)))
    assertEquals("1${NBSP}час 21${NBSP}минута", getTimeText(hm(13, 21)))
  }

  @Test
  fun getTimeText_Verbose() = with(RussianTimeSystem(verbose = true, twentyFourHours = false)) {
    assertEquals("двенадцать${NBSP}часов ноль${NBSP}минут", getTimeText(hm(0, 0)))
    assertEquals("двенадцать${NBSP}часов одна${NBSP}минута", getTimeText(hm(0, 1)))
    assertEquals("двенадцать${NBSP}часов ноль${NBSP}минут", getTimeText(hm(12, 0)))
    assertEquals("двенадцать${NBSP}часов тридцать${NBSP}четыре${NBSP}минуты", getTimeText(hm(12, 34)))
    assertEquals("один${NBSP}час двадцать${NBSP}одна${NBSP}минута", getTimeText(hm(13, 21)))
  }

  @Test
  fun getTimeText_TwentyFour() = with(RussianTimeSystem(verbose = false, twentyFourHours = true)) {
    assertEquals("0${NBSP}часов 0${NBSP}минут", getTimeText(hm(0, 0)))
    assertEquals("0${NBSP}часов 1${NBSP}минута", getTimeText(hm(0, 1)))
    assertEquals("12${NBSP}часов 0${NBSP}минут", getTimeText(hm(12, 0)))
    assertEquals("12${NBSP}часов 34${NBSP}минуты", getTimeText(hm(12, 34)))
    assertEquals("13${NBSP}часов 21${NBSP}минута", getTimeText(hm(13, 21)))
  }

  @Test
  fun getTimeText_Verbose_TwentyFour() = with(RussianTimeSystem(verbose = true, twentyFourHours = true)) {
    assertEquals("ноль${NBSP}часов ноль${NBSP}минут", getTimeText(hm(0, 0)))
    assertEquals("ноль${NBSP}часов одна${NBSP}минута", getTimeText(hm(0, 1)))
    assertEquals("двенадцать${NBSP}часов ноль${NBSP}минут", getTimeText(hm(12, 0)))
    assertEquals("двенадцать${NBSP}часов тридцать${NBSP}четыре${NBSP}минуты", getTimeText(hm(12, 34)))
    assertEquals("тринадцать${NBSP}часов двадцать${NBSP}одна${NBSP}минута", getTimeText(hm(13, 21)))
  }
}