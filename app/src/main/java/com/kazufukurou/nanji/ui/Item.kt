package com.kazufukurou.nanji.ui

import androidx.annotation.StringRes
import kotlin.reflect.KMutableProperty0

sealed class Item(@StringRes val title: Int)

class ActionItem(
  title: Int,
  val onClick: () -> Unit
) : Item(title)

class SwitchItem(
  title: Int,
  val property: KMutableProperty0<Boolean>
) : Item(title)

class EditItem(
  title: Int,
  val message: String,
  val property: KMutableProperty0<String>
) : Item(title)

class SelectorItem(
  title: Int,
  val items: List<String>,
  val indexProperty: KMutableProperty0<Int>
) : Item(title)

