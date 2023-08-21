package com.kazufukurou.nanji.model

import androidx.annotation.StringRes
import com.kazufukurou.nanji.R

enum class Language(@StringRes val title: Int) {
  system(R.string.languageSystem),
  zhCN(R.string.languageZhCn),
  zhTW(R.string.languageZhTw),
  ja(R.string.languageJa),
  ko(R.string.languageKo),
  ru(R.string.languageRu),
  en(R.string.languageEn)
}