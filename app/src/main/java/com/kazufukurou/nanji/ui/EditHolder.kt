package com.kazufukurou.nanji.ui

import android.app.Dialog
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.artyommironov.anyadapter.AnyHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    MaterialAlertDialogBuilder(context)
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
