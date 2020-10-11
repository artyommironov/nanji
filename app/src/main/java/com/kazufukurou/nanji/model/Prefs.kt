/*
 * Copyright 2019 Artyom Mironov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kazufukurou.nanji.model

import android.content.SharedPreferences
import android.graphics.Color
import com.kazufukurou.kprefs.boolean
import com.kazufukurou.kprefs.enum
import com.kazufukurou.kprefs.int
import com.kazufukurou.kprefs.string
import com.kazufukurou.nanji.R
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
  @Deprecated("use textSize") var smallTextSize by prefs.boolean()
  @Deprecated("use showWords") var showDigits by prefs.boolean()
  @Deprecated("use tapAction") var openClock by prefs.boolean()
}
