package com.kazufukurou.nanji.model

import android.content.SharedPreferences
import android.graphics.Color
import androidx.core.content.edit
import com.artyommironov.kprefs.property

class Prefs(prefs: SharedPreferences) {
  val bgColorDef: Int = Color.argb(192, 0, 0, 0)
  val textColorDef: Int = Color.WHITE
  val cornerRadiusDefault: Int = 8
  val cornerRadiusRange: IntRange = 0..20
  val textSizeDefault: Int = 24
  val textSizeRange: IntRange = 16..36
  var bgColor: Int by prefs.property(bgColorDef)
  var textColor: Int by prefs.property(textColorDef)
  var cornerRadius: Int by prefs.property(cornerRadiusDefault)
  var textSize: Int by prefs.property(textSizeDefault)
  var fullWidthDigits: Boolean by prefs.property(false)
  var language: Language by prefs.property(Language.system)
  var tapAction: TapAction by prefs.property(TapAction.ShowWords)
  var dateTimeDisplayMode: DateTimeDisplayMode by prefs.property(DateTimeDisplayMode.DateTime)
  var twentyFour: Boolean by prefs.property(false)
  var japaneseEra: Boolean by prefs.property(false)
  var showWords: Boolean by prefs.property(false)
  var showBattery: Boolean by prefs.property(false)
  var customSymbols: String by prefs.property("")
  var timeZone: String by prefs.property("")

  init {
    prefs.edit {
      val oldKeys = listOf("smallTextSize", "showDigits", "openClock", "hideTime")
      oldKeys.forEach(::remove)
    }
  }
}
