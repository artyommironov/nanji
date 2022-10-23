package com.kazufukurou.nanji.model

import android.content.Context
import androidx.preference.PreferenceManager

fun Context.getPrefs() = Prefs(PreferenceManager.getDefaultSharedPreferences(this))