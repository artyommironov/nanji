package com.kazufukurou.nanji.model

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.Locale

object Module {
  fun getPrefs(context: Context): Prefs = Prefs(PreferenceManager.getDefaultSharedPreferences(context))

  fun getTimeSystem(prefs: Prefs): TimeSystem {
    val useWords = prefs.showWords
    val useTwentyFourHours = prefs.twentyFour
    return when (prefs.language) {
      Language.zhCN -> ChineseTimeSystem(simplified = true, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.zhTW -> ChineseTimeSystem(simplified = false, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ja -> JapaneseTimeSystem(useEra = prefs.japaneseEra, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ko -> KoreanTimeSystem(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.en -> EnglishTimeSystem(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ru -> RussianTimeSystem(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.system -> DefaultTimeSystem(Locale.getDefault(), useTwentyFourHours = useTwentyFourHours)
    }
  }
}
