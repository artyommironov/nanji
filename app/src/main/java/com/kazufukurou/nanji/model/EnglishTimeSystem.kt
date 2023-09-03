package com.kazufukurou.nanji.model

import java.util.Calendar
import java.util.Locale

class EnglishTimeSystem(
  private val verbose: Boolean,
  private val twentyFourHours: Boolean
) : TimeSystem {
  private val defaultTimeSystem = DefaultTimeSystem(Locale.ENGLISH, twentyFourHours = twentyFourHours)

  override val verboseComponents: Set<DateTimeComponent> = setOf(DateTimeComponent.Time)

  override fun getPercentText(value: Int): String = defaultTimeSystem.getPercentText(value)

  override fun getDateText(cal: Calendar): String = defaultTimeSystem.getDateText(cal)

  override fun getTimeText(cal: Calendar): String {
    val (h, m) = cal.run { hourOfDay to minute }
    return when {
      twentyFourHours && !verbose -> "$h".padStart(2, '0') + ":" + "$m".padStart(2, '0')
      twentyFourHours -> {
        val hour = h.toWords()
          .let { if (h < 10 && !(h == 0 && m == 0)) "${0.word}$NBSP$it" else it }
        val minute = m.toWords()
          .let { if (m == 0) 100.word else if (m < 10) "${0.word}$NBSP$it" else it }
        "$hour $minute"
      }
      else -> {
        val (hr, mr) = when {
          m > 30 -> ((cal.hour12 + 1).takeIf { it < 13 } ?: 1) to (60 - m)
          else -> cal.hour12 to m
        }
        val minute = mr.toWords() + NBSP + (if (mr == 1) "minute" else "minutes")
        val hour = hr.toWords()
        val prefix = if (m > 30) "to" else "past"
        "It's " + when (mr) {
          0 -> "$hour${NBSP}o'clock"
          15 -> "quarter $prefix$NBSP$hour"
          30 -> "half $prefix$NBSP$hour"
          else -> "$minute $prefix$NBSP$hour"
        }
      }
    }
  }

  private val Int.word: String get() = when (this) {
    0 -> "zero"
    1 -> "one"
    2 -> "two"
    3 -> "three"
    4 -> "four"
    5 -> "five"
    6 -> "six"
    7 -> "seven"
    8 -> "eight"
    9 -> "nine"
    10 -> "ten"
    11 -> "eleven"
    12 -> "twelve"
    13 -> "thirteen"
    14 -> "fourteen"
    15 -> "fifteen"
    16 -> "sixteen"
    17 -> "seventeen"
    18 -> "eighteen"
    19 -> "nineteen"
    20 -> "twenty"
    30 -> "thirty"
    40 -> "forty"
    50 -> "fifty"
    100 -> "hundred"
    else -> ""
  }

  private fun Int.toWords(): String = if (verbose) toWordsEnRu { it.word } else toString()
}
