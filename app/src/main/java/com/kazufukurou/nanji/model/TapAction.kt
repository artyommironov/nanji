package com.kazufukurou.nanji.model

import androidx.annotation.StringRes
import com.kazufukurou.nanji.R

enum class TapAction(@StringRes val title: Int) {
  ShowWords(R.string.tapActionToggleVerboseDisplay),
  OpenClock(R.string.tapActionOpenClock),
  OpenSetting(R.string.tapActionOpenSettings)
}