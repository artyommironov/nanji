package com.kazufukurou.nanji.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {
  @Test
  fun toCodePoints() {
    assertEquals(listOf("0", "０", "1", "１", "2", "２", ":", "：", "~", "～"), "0０1１2２:：~～".toCodePoints())
  }
}
