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
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.kazufukurou.anyadapter.AnyHolder
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.dp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*

class EditHolder(
  parent: ViewGroup,
  private val dialogShower: (Dialog) -> Unit
) : AnyHolder<EditItem>(parent, R.layout.item), LayoutContainer {
  override val containerView: View = itemView

  init {
    switchValue.isVisible = false
    containerView.setOnClickListener { showEditAlert(currentItem) }
  }

  override fun onBind(item: EditItem) {
    textTitle.setText(item.title)
    textSubTitle.text = item.property.get()
    textSubTitle.isVisible = textSubTitle.text.toString().isNotEmpty()
  }

  private fun showEditAlert(item: EditItem) {
    val edit = EditText(context).apply {
      setSingleLine()
      append(item.property.get())
    }
    AlertDialog.Builder(context)
      .setTitle(item.title)
      .apply { if (item.message.isNotEmpty()) setMessage(item.message) }
      .setView(FrameLayout(context).apply {
        setPadding(resources.dp(16), 0, resources.dp(16), 0)
        addView(edit)
      })
      .setPositiveButton(android.R.string.ok) { _, _ ->
        item.property.set(edit.text.toString().trim())
        onBind(item)
      }
      .setNegativeButton(android.R.string.cancel, null)
      .create()
      .let(dialogShower)
  }
}
