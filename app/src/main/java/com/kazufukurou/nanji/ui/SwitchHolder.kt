package com.kazufukurou.nanji.ui

import androidx.core.view.isVisible
import com.artyommironov.anyadapter.AnyHolder
import com.kazufukurou.nanji.databinding.ItemBinding

class SwitchHolder(private val binding: ItemBinding) : AnyHolder<SwitchItem>(binding.root) {
  init {
    with(binding) {
      root.setOnClickListener {
        currentItem.property.set(!currentItem.property.get())
        onBind(currentItem)
      }
      textSubTitle.isVisible = false
      switchValue.isClickable = false
    }
  }

  override fun onBind(item: SwitchItem) = with(binding) {
    textTitle.setText(item.title)
    switchValue.isChecked = item.property.get()
  }
}