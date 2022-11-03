package com.kazufukurou.nanji.model

import android.content.SharedPreferences
import android.graphics.Color
import androidx.core.content.edit

class Prefs(private val prefs: SharedPreferences) {
  private val bgColorDef: Int = Color.argb(192, 0, 0, 0)
  private val textColorDef: Int = Color.WHITE
  private val cornerRadiusDefault: Int = 8
  private val textSizeDefault: Int = 24
  val cornerRadiusRange: IntRange = 0..20
  val textSizeRange: IntRange = 16..36
  var bgColor: Int by prefs.property(bgColorDef)
  var textColor: Int by prefs.property(textColorDef)
  var cornerRadius: Int by prefs.property(cornerRadiusDefault)
  var textSize: Int by prefs.property(textSizeDefault)
  var fullWidthCharacters: Boolean by prefs.property(false)
  var language: Language by prefs.property(Language.system)
  var tapAction: TapAction by prefs.property(TapAction.ShowWords)
  var dateTimeDisplayMode: DateTimeDisplayMode by prefs.property(DateTimeDisplayMode.DateTime)
  var twentyFour: Boolean by prefs.property(false)
  var japaneseEra: Boolean by prefs.property(false)
  var showWords: Boolean by prefs.property(false)
  var showBattery: Boolean by prefs.property(false)
  var batteryLevelPrefix: String by prefs.property("~")
  var timeZone: String by prefs.property("")

  init {
    prefs.edit {
      val oldKeys = listOf(
        "smallTextSize",
        "showDigits",
        "openClock",
        "hideTime",
        "customSymbols",
        "fullWidthDigits",
      )
      oldKeys.forEach(::remove)
    }
  }

  fun clear() = prefs.edit().clear().apply()

  fun clearAppearance() {
    bgColor = bgColorDef
    textColor = textColorDef
    cornerRadius = cornerRadiusDefault
    textSize = textSizeDefault
    fullWidthCharacters = false
  }
}
