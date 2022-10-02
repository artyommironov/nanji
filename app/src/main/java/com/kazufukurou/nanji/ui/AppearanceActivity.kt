package com.kazufukurou.nanji.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.Menu
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.artyommironov.colorpicker.ColorPicker
import com.artyommironov.colorpicker.ColorTextWatcher
import com.artyommironov.colorpicker.SquareTileDrawable
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.databinding.AppearanceBinding
import com.kazufukurou.nanji.model.Prefs
import kotlin.properties.Delegates

class AppearanceActivity : AppCompatActivity() {
  private val colorTextWatcher by lazy(LazyThreadSafetyMode.NONE) { ColorTextWatcher(binding.colorPicker) }
  private val prefs by lazy(LazyThreadSafetyMode.NONE) { Prefs(PreferenceManager.getDefaultSharedPreferences(this)) }
  private var isText by Delegates.observable(false) { _, old, new -> if (new != old) init() }
  private var textColor by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var bgColor by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var cornerRadius by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var textSize by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var fullWidthDigits by Delegates.observable(false) { _, old, new -> if (new != old) render() }
  private lateinit var binding: AppearanceBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = AppearanceBinding.inflate(layoutInflater)
    setContentView(binding.root)

    bgColor = prefs.bgColor
    textColor = prefs.textColor
    cornerRadius = prefs.cornerRadius.coerceIn(prefs.cornerRadiusRange)
    textSize = prefs.textSize.coerceIn(prefs.textSizeRange)
    fullWidthDigits = prefs.fullWidthDigits

    ViewCompat.setBackground(binding.viewSampleBg, SquareTileDrawable(resources.dp(8), Color.WHITE, Color.LTGRAY))
    binding.viewSampleBg.setOnClickListener { isText = false }
    binding.buttonText.setOnClickListener { isText = true }
    binding.switchWideText.setOnClickListener { fullWidthDigits = !fullWidthDigits }
    binding.radioBg.setOnClickListener { isText = false }
    binding.radioText.setOnClickListener { isText = true }
    with(binding.seekTextSize) {
      val min = prefs.textSizeRange.first
      max = prefs.textSizeRange.last - min
      onProgressChange { fromUser, progress -> if (fromUser) textSize = min + progress }
    }
    with(binding.seekCornerRadius) {
      val min = prefs.cornerRadiusRange.first
      max = prefs.cornerRadiusRange.last - min
      onProgressChange { fromUser, progress -> if (fromUser) cornerRadius = min + progress }
    }
    binding.colorPicker.setup(
      mode = ColorPicker.Mode.RGBA,
      onColorChange = {
        val color = binding.colorPicker.color
        if (isText) textColor = color else bgColor = color
      }
    )
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
    binding.seekCornerRadius.progress = cornerRadius - prefs.cornerRadiusRange.first
    binding.seekTextSize.progress = textSize - prefs.textSizeRange.first
    binding.colorPicker.color = if (isText) textColor else bgColor
    render()
  }

  private fun render() {
    colorTextWatcher.updateEditText(binding.editColor)
    val textSize = resources.dp(textSize).toFloat()
    val radius = resources.dp(cornerRadius).toFloat()
    with(binding.textSample) {
      text = if (fullWidthDigits) "１２" else "12"
      setTextColor(textColor)
      setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
      ViewCompat.setBackground(
        this,
        GradientDrawable().also {
          it.setColor(bgColor)
          it.cornerRadius = radius
          it.setBounds(0, 0, width, height)
        }
      )
    }
    binding.switchWideText.isChecked = fullWidthDigits
    binding.radioBg.isChecked = !isText
    binding.radioText.isChecked = isText
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


  private fun SeekBar.onProgressChange(action : (fromUser: Boolean, progress: Int) -> Unit) {
    setOnSeekBarChangeListener(
      object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekbar: SeekBar) {}
        override fun onStopTrackingTouch(seekbar: SeekBar) {}
        override fun onProgressChanged(seekbar: SeekBar, progress: Int, fromUser: Boolean) = action(fromUser,progress)
      }
    )
  }
}
