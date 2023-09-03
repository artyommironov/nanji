package com.kazufukurou.nanji.model

import androidx.annotation.StringRes
import com.kazufukurou.nanji.R

enum class DateTimeDisplayMode(@StringRes val title: Int, val components: Set<DateTimeComponent>) {
  DateTime(R.string.dateTimeDisplayModeDateTime, setOf(DateTimeComponent.Date, DateTimeComponent.Time)),
  TimeDate(R.string.dateTimeDisplayModeTimeDate, setOf(DateTimeComponent.Time, DateTimeComponent.Date)),
  DateOnly(R.string.dateTimeDisplayModeDateOnly, setOf(DateTimeComponent.Date)),
  TimeOnly(R.string.dateTimeDisplayModeTimeOnly, setOf(DateTimeComponent.Time));
}