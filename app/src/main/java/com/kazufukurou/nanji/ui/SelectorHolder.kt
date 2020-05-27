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

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.kazufukurou.anyadapter.AnyHolder
import com.kazufukurou.nanji.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*

class SelectorHolder(
  parent: ViewGroup,
  private val dialogShower: (Dialog) -> Unit
) : AnyHolder<SelectorItem>(parent, R.layout.item), LayoutContainer {
  override val containerView: View = itemView

  init {
    switchValue.isVisible = false
    containerView.setOnClickListener { showSelectorAlert(currentItem) }
  }

  override fun onBind(item: SelectorItem) {
    textTitle.setText(item.title)
    textSubTitle.text = item.items[item.indexProperty.get()]
    textSubTitle.isVisible = textSubTitle.text.toString().isNotEmpty()
  }

  private fun showSelectorAlert(item: SelectorItem) {
    AlertDialog.Builder(context)
      .setTitle(item.title)
      .setSingleChoiceItems(
        item.items.toTypedArray(),
        item.indexProperty.get().coerceAtLeast(0)
      ) { d, w ->
        item.indexProperty.set(w)
        d.cancel()
        onBind(item)
      }
      .create()
      .let(dialogShower)
  }
}
