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

package com.kazufukurou.nanji.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.kazufukurou.colorpicker.ColorTextWatcher
import com.kazufukurou.colorpicker.SquareTileDrawable
import com.kazufukurou.nanji.model.Prefs
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.dp
import com.kazufukurou.nanji.onProgressChange
import kotlinx.android.synthetic.main.appearance.*
import kotlin.properties.Delegates

class AppearanceActivity : AppCompatActivity() {
  private val colorTextWatcher by lazy(LazyThreadSafetyMode.NONE) { ColorTextWatcher(colorPicker) }
  private val prefs by lazy(LazyThreadSafetyMode.NONE) { Prefs(PreferenceManager.getDefaultSharedPreferences(this)) }
  private var isText by Delegates.observable(false) { _, old, new -> if (new != old) init() }
  private var textColor by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var bgColor by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var cornerRadius by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var textSize by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var fullWidthDigits by Delegates.observable(false) { _, old, new -> if (new != old) render() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.appearance)

    bgColor = prefs.bgColor
    textColor = prefs.textColor
    cornerRadius = prefs.cornerRadius.coerceIn(prefs.cornerRadiusRange)
    textSize = prefs.textSize.coerceIn(prefs.textSizeRange)
    fullWidthDigits = prefs.fullWidthDigits

    ViewCompat.setBackground(viewSampleBg, SquareTileDrawable(resources.dp(8), Color.WHITE, Color.LTGRAY))
    viewSampleBg.setOnClickListener { isText = false }
    buttonText.setOnClickListener { isText = true }
    switchWideText.setOnClickListener { fullWidthDigits = !fullWidthDigits }
    radioBg.setOnClickListener { isText = false }
    radioText.setOnClickListener { isText = true }
    with(seekTextSize) {
      val min = prefs.textSizeRange.first
      max = prefs.textSizeRange.last - min
      onProgressChange { fromUser, progress -> if (fromUser) textSize = min + progress }
    }
    with(seekCornerRadius) {
      val min = prefs.cornerRadiusRange.first
      max = prefs.cornerRadiusRange.last - min
      onProgressChange { fromUser, progress -> if (fromUser) cornerRadius = min + progress }
    }
    colorPicker.onColorChange = {
      val color = colorPicker.color
      if (isText) textColor = color else bgColor = color
    }
    init()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menu?.add(R.string.reset)?.setOnMenuItemClickListener {
      reset()
      true
    }
    return true
  }

  override fun onPause() {
    super.onPause()
    save()
  }

  private fun init() {
    seekCornerRadius.progress = cornerRadius - prefs.cornerRadiusRange.first
    seekTextSize.progress = textSize - prefs.textSizeRange.first
    colorPicker.color = if (isText) textColor else bgColor
    render()
  }

  private fun render() {
    colorTextWatcher.updateEditText(editColor)
    val textSize = resources.dp(textSize).toFloat()
    val radius = resources.dp(cornerRadius).toFloat()
    with(textSample) {
      text = if (fullWidthDigits) "１２" else "12"
      setTextColor(textColor)
      setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
      ViewCompat.setBackground(
        this,
        GradientDrawable().apply {
          setColor(bgColor)
          cornerRadius = radius
          setBounds(0, 0, textSample.width, textSample.height)
        }
      )
    }
    switchWideText.isChecked = fullWidthDigits
    radioBg.isChecked = !isText
    radioText.isChecked = isText
  }

  private fun reset() {
    isText = false
    bgColor = prefs.bgColorDef
    textColor = prefs.textColorDef
    cornerRadius = prefs.cornerRadiusDefault
    textSize = prefs.textSizeDefault
    fullWidthDigits = false
    init()
  }

  private fun save() {
    prefs.bgColor = bgColor
    prefs.textColor = textColor
    prefs.cornerRadius = cornerRadius
    prefs.textSize = textSize
    prefs.fullWidthDigits = fullWidthDigits
  }
}
