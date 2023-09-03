package com.kazufukurou.nanji.model

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.Locale

object Module {
  fun getPrefs(context: Context): Prefs = Prefs(PreferenceManager.getDefaultSharedPreferences(context))

  fun getTimeSystem(prefs: Prefs): TimeSystem {
    val verbose = prefs.showWords
    val twentyFourHours = prefs.twentyFour
    return when (prefs.language) {
      Language.zhCN -> ChineseTimeSystem(simplified = true, verbose = verbose, twentyFourHours = twentyFourHours)
      Language.zhTW -> ChineseTimeSystem(simplified = false, verbose = verbose, twentyFourHours = twentyFourHours)
      Language.ja -> JapaneseTimeSystem(era = prefs.japaneseEra, verbose = verbose, twentyFourHours = twentyFourHours)
      Language.ko -> KoreanTimeSystem(verbose = verbose, twentyFourHours = twentyFourHours)
      Language.en -> EnglishTimeSystem(verbose = verbose, twentyFourHours = twentyFourHours)
      Language.ru -> RussianTimeSystem(verbose = verbose, twentyFourHours = twentyFourHours)
      Language.system -> DefaultTimeSystem(Locale.getDefault(), twentyFourHours = twentyFourHours)
    }
  }
}
