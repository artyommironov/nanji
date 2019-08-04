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

package com.kazufukurou.nanji

class CJKNumberToTextConverter(private val wordSource: (Int) -> String) {

  fun convert(num: Int): String = StringBuilder().apply {
    when (num) {
      in 0..10 -> append(wordSource(num))
      in 11..19 -> append(wordSource(10)).append(convert(num - 10))
      in 20..99 -> append(wordSource(num / 10)).append(convert(num - 10 * (num / 10 - 1)))
      100 -> append(wordSource(100))
      in 101..199 -> append(wordSource(100)).append(convert(num - 100))
      in 200..999 -> append(wordSource(num / 100)).append(convert(num - 100 * (num / 100 - 1)))
      1000 -> append(wordSource(1000))
      in 1001..1999 -> append(wordSource(1000)).append(convert(num - 1000))
      in 2000..9999 -> append(wordSource(num / 1000)).append(convert(num - 1000 * (num / 1000 - 1)))
      else -> Unit
    }
  }.toString()
}