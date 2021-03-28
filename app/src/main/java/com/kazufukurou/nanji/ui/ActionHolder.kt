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

import androidx.core.view.isVisible
import com.kazufukurou.anyadapter.AnyHolder
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