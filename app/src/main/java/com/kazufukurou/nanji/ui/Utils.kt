/*
 * Copyright 2021 Artyom Mironov
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

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.widget.RemoteViews
import com.kazufukurou.nanji.R
import kotlin.math.roundToInt

fun Resources.dp(dp: Int): Int = (dp * displayMetrics.density).roundToInt()

fun RemoteViews.drawBg(bgColor: Int, bmpSize: Int, cornerRadius: Int) {
  val paint = Paint().apply {
    isAntiAlias = true
    style = Paint.Style.FILL
    color = bgColor
  }
  val radius = cornerRadius.toFloat()
  val size = bmpSize.toFloat()
  val config = Bitmap.Config.ARGB_8888

  listOf(
    R.id.imageBgRightBottom to RectF(-size, -size, size, size),
    R.id.imageBgLeftBottom to RectF(0f, -size, size * 2f, size),
    R.id.imageBgLeftTop to RectF(0f, 0f, size * 2f, size * 2f),
    R.id.imageBgRightTop to RectF(-size, 0f, size, size * 2f)
  ).forEach { (id, rect) ->
    val bmp = Bitmap.createBitmap(bmpSize, bmpSize, config)
    Canvas(bmp).drawRoundRect(rect, radius, radius, paint)
    setImageViewBitmap(id, bmp)
  }
  val bmp = Bitmap.createBitmap(2, 2, config)
  Canvas(bmp).drawColor(bgColor)
  setImageViewBitmap(R.id.imageBgTop, bmp)
  setImageViewBitmap(R.id.imageBgMiddle, bmp)
  setImageViewBitmap(R.id.imageBgBottom, bmp)
}
