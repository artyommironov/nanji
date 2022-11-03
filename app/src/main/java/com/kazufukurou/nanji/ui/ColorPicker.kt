package com.kazufukurou.nanji.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.widget.addTextChangedListener

class ColorPicker : View {
  private val paint = Paint()
  private val rects: Array<RectF> = Array(4) { RectF() }
  private val hsv: FloatArray = listOf(Bar.H, Bar.S, Bar.V).map { it.max.toFloat() }.toFloatArray()
  private var argb: Int = Color.WHITE
  private var currentBar: Bar? = null
  private var barHeight: Int = 0
  private var barSpacing: Int = 0
  private var alphaBackground: Drawable? = null
  private var thumb: Drawable? = null
  private var mode: Mode = Mode.HSVA
  private var editText: EditText? = null
  private var editTextOwner: EditTextOwner = EditTextOwner.None
  private var onColorChange: (() -> Unit)? = null
  val color: Int
    @ColorInt get() = when (mode) {
      Mode.HSV, Mode.HSVA -> hsv.toColor(a = argb.alpha)
      else -> argb
    }

  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

  init {
    paint.style = Paint.Style.FILL
    paint.isAntiAlias = true
    setWillNotDraw(false)
  }

  fun setup(
    @ColorInt color: Int,
    mode: Mode = Mode.HSVA,
    @Px barHeight: Int,
    @Px barSpacing: Int,
    alphaBackground: Drawable,
    thumb: Drawable,
    editText: EditText? = null,
    onColorChange: (() -> Unit)? = null
  ) {
    this.mode = mode
    this.barHeight = barHeight
    this.barSpacing = barSpacing
    this.alphaBackground = alphaBackground
    this.thumb = thumb
    this.onColorChange = onColorChange
    this.editText = editText
    editText?.addTextChangedListener {
      if (editTextOwner == EditTextOwner.None) {
        editTextOwner = EditTextOwner.Keyboard
        try {
          updateColorValue(Color.parseColor("#${it.toString().padEnd(8, '0')}"))
          onColorChange?.invoke()
        } catch (e: Exception) {
          // ignore
        }
        editTextOwner = EditTextOwner.None
      }
    }
    updateColorValue(color)
    updateEditText()
    requestLayout()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    mode.getBars().forEach {
      drawBar(canvas, it)
      drawThumb(canvas, it)
    }
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    rects.forEachIndexed { index, rect ->
      rect.set(
        paddingLeft.toFloat(),
        (paddingTop + barHeight * index + barSpacing * index).toFloat(),
        (width - paddingRight).toFloat(),
        (paddingTop + barHeight * (index + 1) + barSpacing * index).toFloat()
      )
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val w = MeasureSpec.getSize(widthMeasureSpec)
    val h = when (mode) {
      Mode.A -> barHeight
      Mode.HSV, Mode.RGB -> barHeight * 3 + barSpacing * 2
      Mode.HSVA, Mode.RGBA -> barHeight * 4 + barSpacing * 3
    } + paddingTop + paddingBottom
    setMeasuredDimension(w, h)
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    currentBar = when (event.action) {
      MotionEvent.ACTION_DOWN -> mode.getBars().find { getRect(it).contains(event.x, event.y) }
      MotionEvent.ACTION_MOVE -> currentBar
      else -> null
    }
    val bar = currentBar ?: return super.onTouchEvent(event)
    val rect = getRect(bar).takeIf { it.width() > 0 } ?: return super.onTouchEvent(event)
    setBarValue(bar, (event.x - rect.left).coerceIn(0f, rect.width()) * bar.max / rect.width())
    invalidate()
    updateEditText()
    onColorChange?.invoke()
    return true
  }

  private fun updateColorValue(@ColorInt color: Int) {
    argb = color
    Color.RGBToHSV(color.red, color.green, color.blue, hsv)
    invalidate()
  }

  private fun updateEditText() {
    val edit = editText ?: return
    if (editTextOwner == EditTextOwner.None) {
      editTextOwner = EditTextOwner.Picker
      edit.setText("")
      edit.append(Integer.toHexString(color).padStart(8, '0'))
      editTextOwner = EditTextOwner.None
    }
  }

  private fun drawThumb(canvas: Canvas, bar: Bar) {
    val rect = getRect(bar)
    val thumbRadius = (thumb?.intrinsicWidth ?: 0) * 0.5f
    val x = getBarValue(bar) * rect.width() / bar.max + rect.left
    val left = (x - thumbRadius).coerceIn(rect.left, rect.right - thumbRadius * 2f)
    val right = (x + thumbRadius).coerceIn(rect.left + thumbRadius * 2f, rect.right)
    thumb?.setBounds(left.toInt(), rect.top.toInt(), right.toInt(), rect.bottom.toInt())
    thumb?.draw(canvas)
  }

  private fun drawBar(canvas: Canvas, bar: Bar) {
    val rect = getRect(bar)
    if (bar == Bar.A) {
      alphaBackground?.setBounds(rect.left.toInt(), rect.top.toInt(), rect.right.toInt(), rect.bottom.toInt())
      alphaBackground?.draw(canvas)
    }
    val colors = when (bar) {
      Bar.H -> IntArray(Bar.H.max) { hsv.toColor(a = Bar.A.max, h = it.toFloat()) }
      Bar.S -> intArrayOf(hsv.toColor(a = Bar.A.max, s = 0f), hsv.toColor(a = Bar.A.max, s = Bar.S.max.toFloat()))
      Bar.V -> intArrayOf(hsv.toColor(a = Bar.A.max, v = 0f), hsv.toColor(a = Bar.A.max, v = Bar.V.max.toFloat()))
      Bar.R -> intArrayOf(argb.copy(a = Bar.A.max, r = 0), argb.copy(a = Bar.A.max, r = Bar.R.max))
      Bar.G -> intArrayOf(argb.copy(a = Bar.A.max, g = 0), argb.copy(a = Bar.A.max, g = Bar.G.max))
      Bar.B -> intArrayOf(argb.copy(a = Bar.A.max, b = 0), argb.copy(a = Bar.A.max, b = Bar.B.max))
      Bar.A -> intArrayOf(argb.copy(a = 0), argb.copy(a = Bar.A.max))
    }
    paint.shader = when (colors.size == 2) {
      true -> LinearGradient(rect.left, rect.top, rect.right, rect.top, colors[0], colors[1], Shader.TileMode.CLAMP)
      else -> LinearGradient(rect.left, rect.top, rect.right, rect.top, colors, null, Shader.TileMode.CLAMP)
    }
    canvas.drawRect(rect, paint)
  }

  private fun getBarValue(bar: Bar): Float = when (bar) {
    Bar.H -> hsv[0]
    Bar.S -> hsv[1]
    Bar.V -> hsv[2]
    Bar.R -> argb.red.toFloat()
    Bar.G -> argb.green.toFloat()
    Bar.B -> argb.blue.toFloat()
    Bar.A -> argb.alpha.toFloat()
  }

  private fun setBarValue(bar: Bar, value: Float) {
    when (bar) {
      Bar.H -> hsv[0] = value
      Bar.S -> hsv[1] = value
      Bar.V -> hsv[2] = value
      Bar.R -> argb = argb.copy(r = value.toInt())
      Bar.G -> argb = argb.copy(g = value.toInt())
      Bar.B -> argb = argb.copy(b = value.toInt())
      Bar.A -> argb = argb.copy(a = value.toInt())
    }
    when (bar) {
      Bar.H, Bar.S, Bar.V -> argb = color
      Bar.R, Bar.G, Bar.B -> Color.RGBToHSV(argb.red, argb.green, argb.blue, hsv)
      else -> Unit
    }
  }

  private fun getRect(bar: Bar): RectF = rects[mode.getBars().indexOf(bar)]

  private fun Mode.getBars(): List<Bar> = when (this) {
    Mode.HSVA -> listOf(Bar.H, Bar.S, Bar.V, Bar.A)
    Mode.RGBA -> listOf(Bar.R, Bar.G, Bar.B, Bar.A)
    Mode.HSV -> listOf(Bar.H, Bar.S, Bar.V)
    Mode.RGB -> listOf(Bar.R, Bar.G, Bar.B)
    Mode.A -> listOf(Bar.A)
  }

  private fun FloatArray.toColor(a: Int, h: Float = this[0], s: Float = this[1], v: Float = this[2]): Int {
    return Color.HSVToColor(a, floatArrayOf(h, s, v))
  }

  private fun Int.copy(a: Int = alpha, r: Int = red, g: Int = green, b: Int = blue): Int = Color.argb(a, r, g, b)

  enum class Mode {
    HSVA, RGBA, HSV, RGB, A
  }

  private enum class Bar(val max: Int) {
    H(360), S(1), V(1), R(255), G(255), B(255), A(255)
  }

  private enum class EditTextOwner {
    None, Keyboard, Picker
  }
}