package com.kazufukurou.nanji.model

import androidx.annotation.StringRes
import com.kazufukurou.nanji.R

enum class DateTimeDisplayMode(@StringRes val title: Int) {
  DateTime(R.string.prefsDateTimeDisplayModeDateTime),
  TimeDate(R.string.prefsDateTimeDisplayModeTimeDate),
  DateOnly(R.string.prefsDateTimeDisplayModeDateOnly),
  TimeOnly(R.string.prefsDateTimeDisplayModeTimeOnly)
}