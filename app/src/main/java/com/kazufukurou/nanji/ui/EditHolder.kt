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

import android.app.Dialog
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.artyommironov.anyadapter.AnyHolder
import com.kazufukurou.nanji.databinding.ItemBinding

class EditHolder(
  private val binding: ItemBinding,
  private val dialogShower: (Dialog) -> Unit
) : AnyHolder<EditItem>(binding.root) {
  init {
    with(binding) {
      switchValue.isVisible = false
      root.setOnClickListener { showEditAlert(currentItem) }
    }
  }

  override fun onBind(item: EditItem) = with(binding) {
    textTitle.setText(item.title)
    textSubTitle.text = item.property.get()
    textSubTitle.isVisible = binding.textSubTitle.text.toString().isNotEmpty()
  }

  private fun showEditAlert(item: EditItem) {
    val edit = EditText(context).apply {
      setSingleLine()
      append(item.property.get())
    }
    AlertDialog.Builder(context)
      .setTitle(item.title)
      .apply { if (item.message.isNotEmpty()) setMessage(item.message) }
      .setView(
        FrameLayout(context).apply {
          setPadding(resources.dp(16), 0, resources.dp(16), 0)
          addView(edit)
        }
      )
      .setPositiveButton(android.R.string.ok) { _, _ ->
        item.property.set(edit.text.toString().trim())
        onBind(item)
      }
      .setNegativeButton(android.R.string.cancel, null)
      .create()
      .let(dialogShower)
  }
}
