package com.kazufukurou.nanji.model

import android.content.SharedPreferences
import android.graphics.Color
import androidx.core.content.edit
import com.artyommironov.kprefs.boolean
import com.artyommironov.kprefs.enum
import com.artyommironov.kprefs.int
import com.artyommironov.kprefs.string
import com.kazufukurou.nanji.ui.TapAction

class Prefs(prefs: SharedPreferences) {
  val bgColorDef = Color.argb(192, 0, 0, 0)
  val textColorDef = Color.WHITE
  val cornerRadiusDefault = 8
  val cornerRadiusRange = 0..20
  val textSizeDefault = 24
  val textSizeRange = 16..36
  var bgColor by prefs.int(bgColorDef)
  var textColor by prefs.int(textColorDef)
  var cornerRadius by prefs.int(cornerRadiusDefault)
  var textSize by prefs.int(textSizeDefault)
  var fullWidthDigits by prefs.boolean()
  var hideTime by prefs.boolean()
  var language by prefs.enum(Language.system)
  var tapAction by prefs.enum(TapAction.ShowWords)
  var twentyFour by prefs.boolean()
  var japaneseEra by prefs.boolean()
  var showWords by prefs.boolean()
  var showBattery by prefs.boolean()
  var customSymbols by prefs.string()
  var timeZone by prefs.string()

  init {
    prefs.edit {
      val oldKeys = listOf("smallTextSize", "showDigits", "openClock")
      oldKeys.forEach(::remove)
    }
  }
}
