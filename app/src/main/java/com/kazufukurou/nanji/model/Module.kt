package com.kazufukurou.nanji.model

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.Locale

object Module {
  fun getPrefs(context: Context): Prefs = Prefs(PreferenceManager.getDefaultSharedPreferences(context))

  fun getTimeSystem(prefs: Prefs): TimeSystem {
    val verbose = prefs.showWords
    val useTwentyFourHours = prefs.twentyFour
    return when (prefs.language) {
      Language.zhCN -> ChineseTimeSystem(simplified = true, verbose = verbose, useTwentyFourHours = useTwentyFourHours)
      Language.zhTW -> ChineseTimeSystem(simplified = false, verbose = verbose, useTwentyFourHours = useTwentyFourHours)
      Language.ja -> JapaneseTimeSystem(useEra = prefs.japaneseEra, verbose = verbose, useTwentyFourHours = useTwentyFourHours)
      Language.ko -> KoreanTimeSystem(verbose = verbose, useTwentyFourHours = useTwentyFourHours)
      Language.en -> EnglishTimeSystem(verbose = verbose, useTwentyFourHours = useTwentyFourHours)
      Language.ru -> RussianTimeSystem(verbose = verbose, useTwentyFourHours = useTwentyFourHours)
      Language.system -> DefaultTimeSystem(Locale.getDefault(), useTwentyFourHours = useTwentyFourHours)
    }
  }
}
