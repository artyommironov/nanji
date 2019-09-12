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

private const val kotlinVersion = "1.3.50"
private const val androidxKtxVersion = "1.0.2"
private const val androidxAppcompatVersion = "1.1.0-rc01"
private const val androidxRecyclerviewVersion = "1.0.0"
private const val androidxConstraintlayoutVersion = "1.1.3"
private const val kprefsVersion = "0.0.3"
private const val colorpickerVersion = "1.0.3"
private const val anyadapterVersion = "1.0.0"

object Sdk {
  const val target = 28
  const val compile = 28
}

object Plugins {
  const val android = "com.android.tools.build:gradle:3.3.2"
  const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  const val kotlinAndroidExtensions = "org.jetbrains.kotlin:kotlin-android-extensions:$kotlinVersion"
}

object Libs {
  const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
  const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
  const val androidxKtx = "androidx.core:core-ktx:$androidxKtxVersion"
  const val androidxAppCompat = "androidx.appcompat:appcompat:$androidxAppcompatVersion"
  const val androidxRecyclerView = "androidx.recyclerview:recyclerview:$androidxRecyclerviewVersion"
  const val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:$androidxConstraintlayoutVersion"
  const val kprefs = "com.kazufukurou.kprefs:kprefs:$kprefsVersion"
  const val colorPicker = "com.kazufukurou.colorpicker:colorpicker:$colorpickerVersion"
  const val anyAdapter = "com.kazufukurou.anyadapter:anyadapter:$anyadapterVersion"
}
