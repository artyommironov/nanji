package com.kazufukurou.nanji.ui

import android.content.res.Resources
import kotlin.math.roundToInt

fun Resources.dp(dp: Int): Int = (dp * displayMetrics.density).roundToInt()
