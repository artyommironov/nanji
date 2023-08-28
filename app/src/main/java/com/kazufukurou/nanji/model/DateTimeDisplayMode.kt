package com.kazufukurou.nanji.model

import androidx.annotation.StringRes
import com.kazufukurou.nanji.R

enum class DateTimeDisplayMode(@StringRes val title: Int, val components: Set<DateTimeComponent>) {
  DateTime(R.string.prefsDateTimeDisplayModeDateTime, setOf(DateTimeComponent.Date, DateTimeComponent.Time)),
  TimeDate(R.string.prefsDateTimeDisplayModeTimeDate, setOf(DateTimeComponent.Time, DateTimeComponent.Date)),
  DateOnly(R.string.prefsDateTimeDisplayModeDateOnly, setOf(DateTimeComponent.Date)),
  TimeOnly(R.string.prefsDateTimeDisplayModeTimeOnly, setOf(DateTimeComponent.Time));
}