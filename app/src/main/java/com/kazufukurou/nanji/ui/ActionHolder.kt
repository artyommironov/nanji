package com.kazufukurou.nanji.ui

import androidx.core.view.isVisible
import com.artyommironov.anyadapter.AnyHolder
import com.kazufukurou.nanji.databinding.ItemBinding

class ActionHolder(private val binding: ItemBinding) : AnyHolder<ActionItem>(binding.root) {
  init {
    with(binding) {
      switchValue.isVisible = false
      textSubTitle.isVisible = false
      root.setOnClickListener { currentItem.onClick() }
    }
  }

  override fun onBind(item: ActionItem) = with(binding) {
    textTitle.setText(item.title)
  }
}