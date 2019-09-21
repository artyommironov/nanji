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

private val kotlinVersion = "1.3.50"

object Sdk {
  val target = 28
  val compile = 28
}

object Plugins {
  val android = "com.android.tools.build:gradle:3.5.0"
  val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  val bintray = "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4"
  val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:0.25.0"
}

object Libs {
  val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
  val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
  val androidxKtx = "androidx.core:core-ktx:1.0.2"
  val androidxAppCompat = "androidx.appcompat:appcompat:1.1.0"
  val androidxAnnotation = "androidx.annotation:annotation:1.1.0"
  val androidxRecyclerView = "androidx.recyclerview:recyclerview:1.0.0"
  val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
  val kprefs = "com.kazufukurou.kprefs:kprefs:0.0.3"
  val colorPicker = "com.kazufukurou.colorpicker:colorpicker:1.0.3"
  val anyAdapter = "com.kazufukurou.anyadapter:anyadapter:1.1.0"
}
