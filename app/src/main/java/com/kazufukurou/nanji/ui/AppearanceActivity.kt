package com.kazufukurou.nanji.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.artyommironov.colorpicker.ColorPicker
import com.artyommironov.colorpicker.ColorTextWatcher
import com.artyommironov.colorpicker.SquareTileDrawable
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.databinding.AppearanceBinding
import com.kazufukurou.nanji.model.getPrefs
import kotlin.properties.Delegates

class AppearanceActivity : AppCompatActivity() {
  private val colorTextWatcher by lazy { ColorTextWatcher(binding.colorPicker) }
  private val prefs by lazy { getPrefs() }
  private var isText by Delegates.observable(false) { _, old, new -> if (new != old) init() }
  private var textColor by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var bgColor by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var cornerRadius by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var textSize by Delegates.observable(0) { _, old, new -> if (new != old) render() }
  private var fullWidthCharacters by Delegates.observable(false) { _, old, new -> if (new != old) render() }
  private lateinit var binding: AppearanceBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = AppearanceBinding.inflate(layoutInflater)
    setContentView(binding.root)

    bgColor = prefs.bgColor
    textColor = prefs.textColor
    cornerRadius = prefs.cornerRadius.coerceIn(prefs.cornerRadiusRange)
    textSize = prefs.textSize.coerceIn(prefs.textSizeRange)
    fullWidthCharacters = prefs.fullWidthCharacters

    binding.viewSampleBg.background = SquareTileDrawable(resources.dp(8), Color.WHITE, Color.LTGRAY)
    binding.switchWideText.setOnClickListener { fullWidthCharacters = !fullWidthCharacters }
    binding.buttonBg.setOnClickListener { isText = false }
    binding.buttonText.setOnClickListener { isText = true }
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

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val item = menu.add(R.string.reset)
    item.setIcon(R.drawable.ic_reset)
    MenuItemCompat.setIconTintList(item, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)))
    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    item.setOnMenuItemClickListener {
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
      text = if (fullWidthCharacters) "１２" else "12"
      setTextColor(textColor)
      setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
      background = GradientDrawable().also {
        it.setColor(bgColor)
        it.cornerRadius = radius
        it.setBounds(0, 0, width, height)
      }
    }
    binding.switchWideText.isChecked = fullWidthCharacters
    binding.buttons.check((if (isText) binding.buttonText else binding.buttonBg).id)
  }

  private fun reset() {
    isText = false
    bgColor = prefs.bgColorDef
    textColor = prefs.textColorDef
    cornerRadius = prefs.cornerRadiusDefault
    textSize = prefs.textSizeDefault
    fullWidthCharacters = false
    init()
  }

  private fun save() {
    prefs.bgColor = bgColor
    prefs.textColor = textColor
    prefs.cornerRadius = cornerRadius
    prefs.textSize = textSize
    prefs.fullWidthCharacters = fullWidthCharacters
  }


  private fun SeekBar.onProgressChange(action: (fromUser: Boolean, progress: Int) -> Unit) {
    setOnSeekBarChangeListener(
      object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekbar: SeekBar) {}
        override fun onStopTrackingTouch(seekbar: SeekBar) {}
        override fun onProgressChanged(seekbar: SeekBar, progress: Int, fromUser: Boolean) = action(fromUser, progress)
      }
    )
  }
}
