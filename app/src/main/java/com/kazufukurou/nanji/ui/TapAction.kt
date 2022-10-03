package com.kazufukurou.nanji.ui

import androidx.annotation.StringRes
import com.kazufukurou.nanji.R

enum class TapAction(@StringRes val title: Int) {
  ShowWords(R.string.prefsTapActionToggleVerboseDisplay),
  OpenClock(R.string.prefsTapActionOpenClock),
  OpenSetting(R.string.prefsTapActionOpenSettings)
}