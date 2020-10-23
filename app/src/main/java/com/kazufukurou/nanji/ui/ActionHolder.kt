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

import androidx.core.view.isVisible
import com.kazufukurou.anyadapter.AnyHolder
import com.kazufukurou.nanji.databinding.ItemBinding

class ActionHolder(private val binding: ItemBinding) : AnyHolder<ActionItem>(binding.root) {
  init {
    binding.switchValue.isVisible = false
    binding.textSubTitle.isVisible = false
    binding.root.setOnClickListener { currentItem.onClick() }
  }

  override fun onBind(item: ActionItem) {
    binding.textTitle.setText(item.title)
  }
}