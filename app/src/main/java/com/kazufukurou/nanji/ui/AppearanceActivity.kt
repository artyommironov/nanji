package com.kazufukurou.nanji.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.widget.SeekBar
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.databinding.AppearanceBinding
import com.kazufukurou.nanji.model.Module
import kotlin.properties.Delegates

class AppearanceActivity : AppCompatActivity() {
  private val binding: AppearanceBinding by lazy { AppearanceBinding.inflate(layoutInflater) }
  private val prefs by lazy { Module.getPrefs(this) }
  private val dialogHolder = DialogHolder()
  private var state: State by Delegates.observable(State()) { _, old, new ->
    if (new != old) {
      if (new.isText != old.isText) {
        updatePickers()
      }
      render()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    state = getInitialState()

    binding.viewSampleBg.background = createAlphaBackgroundDrawable()
    binding.switchWideText.setOnClickListener { state = state.copy(fullWidthCharacters = !state.fullWidthCharacters) }
    binding.buttonBg.setOnClickListener { state = state.copy(isText = false) }
    binding.buttonText.setOnClickListener { state = state.copy(isText = true) }
    with(binding.seekTextSize) {
      val min = prefs.textSizeRange.first
      max = prefs.textSizeRange.last - min
      onProgressChange { fromUser, progress -> if (fromUser) state = state.copy(textSizeDp = min + progress) }
    }
    with(binding.seekCornerRadius) {
      val min = prefs.cornerRadiusRange.first
      max = prefs.cornerRadiusRange.last - min
      onProgressChange { fromUser, progress -> if (fromUser) state = state.copy(cornerRadiusDp = min + progress) }
    }
    updatePickers()
    render()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menu.addItem(this, R.string.reset, R.drawable.ic_reset, ::showResetAlert)
    return true
  }

  override fun onPause() {
    super.onPause()
    save()
  }

  private fun showResetAlert() {
    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.reset)
      .setMessage(R.string.resetAppearance)
      .setPositiveButton(R.string.reset) { _, _ ->
        prefs.clearAppearance()
        state = getInitialState()
        updatePickers()
        render()
      }
      .setNegativeButton(android.R.string.cancel, null)
      .create()
      .let(dialogHolder::show)
  }


  private fun updatePickers() = with(binding) {
    seekCornerRadius.progress = state.cornerRadiusDp - prefs.cornerRadiusRange.first
    seekTextSize.progress = state.textSizeDp - prefs.textSizeRange.first
    binding.colorPicker.setup(
      color = if (state.isText) state.textColor else state.bgColor,
      mode = ColorPicker.Mode.RGBA,
      barHeight = resources.dp(32),
      barSpacing = resources.dp(8),
      alphaBackground = createAlphaBackgroundDrawable(),
      thumb = createThumbDrawable(),
      editText = binding.editColor,
      onColorChange = {
        val color = binding.colorPicker.color
        state = if (state.isText) state.copy(textColor = color) else state.copy(bgColor = color)
      }
    )
  }

  private fun getInitialState() = State(
    isText = false,
    bgColor = prefs.bgColor,
    textColor = prefs.textColor,
    textSizeDp = prefs.textSize.coerceIn(prefs.textSizeRange),
    cornerRadiusDp = prefs.cornerRadius.coerceIn(prefs.cornerRadiusRange),
    fullWidthCharacters = prefs.fullWidthCharacters,
  )

  private fun render() {
    val textSize = resources.dp(state.textSizeDp).toFloat()
    val radius = resources.dp(state.cornerRadiusDp).toFloat()
    with(binding.textSample) {
      text = if (state.fullWidthCharacters) "１２" else "12"
      setTextColor(state.textColor)
      setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
      background = GradientDrawable().also {
        it.setColor(state.bgColor)
        it.cornerRadius = radius
        it.setBounds(0, 0, width, height)
      }
    }
    binding.switchWideText.isChecked = state.fullWidthCharacters
    binding.buttons.check((if (state.isText) binding.buttonText else binding.buttonBg).id)
  }

  private fun save() = with(prefs) {
    bgColor = state.bgColor
    textColor = state.textColor
    cornerRadius = state.cornerRadiusDp
    textSize = state.textSizeDp
    fullWidthCharacters = state.fullWidthCharacters
  }


  private fun createThumbDrawable(): Drawable = GradientDrawable().apply {
    setColor(Color.WHITE)
    setStroke(resources.dp(1), Color.BLACK)
    setSize(resources.dp(8), resources.dp(8))
  }

  private fun createAlphaBackgroundDrawable(): Drawable {
    val size = resources.dp(16)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val paint = Paint()
    paint.color = Color.WHITE
    with(Canvas(bitmap)) {
      drawColor(Color.LTGRAY)
      drawRect(0f, 0f, size * 0.5f, size * 0.5f, paint)
      drawRect(size * 0.5f, size * 0.5f, size.toFloat(), size.toFloat(), paint)
    }
    return BitmapDrawable(resources, bitmap).apply {
      setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    }
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

  private data class State(
    val isText: Boolean = false,
    @ColorInt val textColor: Int = Color.TRANSPARENT,
    @ColorInt val bgColor: Int = Color.TRANSPARENT,
    val textSizeDp: Int = 0,
    val cornerRadiusDp: Int = 0,
    val fullWidthCharacters: Boolean = false,
  )
}
