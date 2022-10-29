package com.kazufukurou.nanji.model

import androidx.annotation.StringRes
import com.kazufukurou.nanji.R

enum class DateTimeDisplayMode(@StringRes val title: Int) {
  DateTime(R.string.prefsDateTimeDisplayModeDateTime),
  OnlyDate(R.string.prefsDateTimeDisplayModeOnlyDate),
  OnlyTime(R.string.prefsDateTimeDisplayModeOnlyTime)
}