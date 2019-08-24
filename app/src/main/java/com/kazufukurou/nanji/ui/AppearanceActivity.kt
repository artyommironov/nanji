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

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.Menu
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.kazufukurou.colorpicker.ColorTextWatcher
import com.kazufukurou.colorpicker.SquareTileDrawable
import com.kazufukurou.nanji.model.Prefs
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.dp
import kotlinx.android.synthetic.main.appearance.*
import kotlin.properties.Delegates

class AppearanceActivity : AppCompatActivity() {
  private val colorTextWatcher by lazy(LazyThreadSafetyMode.NONE) { ColorTextWatcher(colorPicker) }
  private val prefs by lazy(LazyThreadSafetyMode.NONE) {
    Prefs(
      PreferenceManager.getDefaultSharedPreferences(
        this
      )
    )
  }
  private var isText by Delegates.observable(false) { _, old, new -> if (new != old) init() }
  private var textColor by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var bgColor by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var cornerRadius by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var smallTextSize by Delegates.observable(false) { _, old, new -> if (new != old) render() }
  private var fullWidthDigits by Delegates.observable(false) { _, old, new -> if (new != old) render() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.appearance)

    bgColor = prefs.bgColor
    textColor = prefs.textColor
    cornerRadius = prefs.cornerRadius.coerceIn(0, seekCornerRadius.max)
    smallTextSize = prefs.smallTextSize
    fullWidthDigits = prefs.fullWidthDigits

    ViewCompat.setBackground(viewSampleBg, SquareTileDrawable(resources.dp(8), Color.WHITE, Color.LTGRAY))
    viewSampleBg.setOnClickListener { isText = false }
    buttonText.setOnClickListener { isText = true }
    switchWideText.setOnClickListener { fullWidthDigits = !fullWidthDigits }
    switchSmallText.setOnClickListener { smallTextSize = !smallTextSize }
    with(seekCornerRadius) {
      max = 20
      setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekbar: SeekBar) {}
        override fun onStopTrackingTouch(seekbar: SeekBar) {}

        override fun onProgressChanged(seekbar: SeekBar, progress: Int, fromUser: Boolean) {
          if (fromUser) cornerRadius = progress
        }
      })
    }
    colorPicker.onColorChange = { if (isText) textColor = colorPicker.color else bgColor = colorPicker.color }
    ObjectAnimator.ofPropertyValuesHolder(
      imageCursor,
      PropertyValuesHolder.ofFloat("scaleX", 1.2f),
      PropertyValuesHolder.ofFloat("scaleY", 1.2f)
    ).apply {
      duration = 300
      repeatCount = ObjectAnimator.INFINITE
      repeatMode = ObjectAnimator.REVERSE
      start()
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
    seekCornerRadius.progress = cornerRadius
    colorPicker.color = if (isText) textColor else bgColor
    render()
  }

  private fun render() {
    colorTextWatcher.updateEditText(editColor)
    val textSizeDimen = if (smallTextSize) R.dimen.textSizeTimeSmall else R.dimen.textSizeTimeNormal
    val radius = resources.dp(cornerRadius).toFloat()
    with(textSample) {
      text = if (fullWidthDigits) "１２" else "12"
      setTextColor(textColor)
      setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(textSizeDimen).toFloat())
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
    switchSmallText.isChecked = smallTextSize
    imageCursor.layoutParams = (imageCursor.layoutParams as ConstraintLayout.LayoutParams).apply {
      topMargin = if (isText) resources.dp(36) else resources.dp(52)
      rightMargin = if (isText) 0 else resources.dp(56)
    }
  }

  private fun reset() {
    isText = false
    bgColor = prefs.bgColorDef
    textColor = prefs.textColorDef
    cornerRadius = prefs.cornerRadiusDef
    smallTextSize = false
    fullWidthDigits = false
    init()
  }

  private fun save() {
    prefs.bgColor = bgColor
    prefs.textColor = textColor
    prefs.cornerRadius = cornerRadius
    prefs.smallTextSize = smallTextSize
    prefs.fullWidthDigits = fullWidthDigits
  }
}
