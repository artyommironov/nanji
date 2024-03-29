package com.kazufukurou.nanji.ui

import androidx.core.view.isVisible
import com.artyommironov.anyadapter.AnyHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kazufukurou.nanji.databinding.ItemBinding

class SelectorHolder(
  private val binding: ItemBinding,
  private val dialogHolder: DialogHolder,
) : AnyHolder<SelectorItem>(binding.root) {
  init {
    with(binding) {
      switchValue.isVisible = false
      root.setOnClickListener { showSelectorAlert(currentItem) }
    }
  }

  override fun onBind(item: SelectorItem) = with(binding) {
    textTitle.setText(item.title)
    textSubTitle.text = item.items[item.indexProperty.get()]
    textSubTitle.isVisible = binding.textSubTitle.text.toString().isNotEmpty()
  }

  private fun showSelectorAlert(item: SelectorItem) {
    MaterialAlertDialogBuilder(context)
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
      .let(dialogHolder::show)
  }
}
