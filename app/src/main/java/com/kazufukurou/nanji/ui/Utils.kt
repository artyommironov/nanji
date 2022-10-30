package com.kazufukurou.nanji.ui

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.kazufukurou.nanji.R
import kotlin.math.roundToInt

fun Resources.dp(dp: Int): Int = (dp * displayMetrics.density).roundToInt()

fun Menu.addItem(
  context: Context,
  @StringRes title: Int,
  @DrawableRes icon: Int,
  onClick: () -> Unit
): MenuItem {
  val item = add(title).setIcon(icon)
  MenuItemCompat.setIconTintList(item, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onPrimary)))
  item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
  item.setOnMenuItemClickListener {
    onClick()
    true
  }
  return item
}
