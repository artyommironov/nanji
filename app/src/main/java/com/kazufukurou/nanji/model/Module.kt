package com.kazufukurou.nanji.model

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.Locale

object Module {
  fun getPrefs(context: Context): Prefs = Prefs(PreferenceManager.getDefaultSharedPreferences(context))

  fun getTime(prefs: Prefs): Time {
    val useWords = prefs.showWords
    val useTwentyFourHours = prefs.twentyFour
    return when (prefs.language) {
      Language.zhCN -> TimeZh(simplified = true, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.zhTW -> TimeZh(simplified = false, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ja -> TimeJa(useEra = prefs.japaneseEra, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ko -> TimeKo(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.en -> TimeEn(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ru -> TimeRu(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.system -> TimeSystem(Locale.getDefault(), useTwentyFourHours = useTwentyFourHours)
    }
  }
}
