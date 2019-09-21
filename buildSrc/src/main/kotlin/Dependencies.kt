private val kotlinVersion = "1.3.50"

object Sdk {
  val compile = 28
  val target = 28
}

object Plugins {
  val android = "com.android.tools.build:gradle:3.5.0"
  val bintray = "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4"
  val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:0.25.0"
  val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
}

object Libs {
  val androidxKtx = "androidx.core:core-ktx:1.0.2"
  val androidxAppCompat = "androidx.appcompat:appcompat:1.1.0"
  val androidxAnnotation = "androidx.annotation:annotation:1.1.0"
  val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
  val androidxRecyclerView = "androidx.recyclerview:recyclerview:1.0.0"
  val androidxTestExtJunit = "androidx.test.ext:junit:1.1.1"
  val anyAdapter = "com.kazufukurou.anyadapter:anyadapter:1.1.0"
  val colorPicker = "com.kazufukurou.colorpicker:colorpicker:1.0.3"
  val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
  val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
  val kprefs = "com.kazufukurou.kprefs:kprefs:0.0.3"
  val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
  val robolectric = "org.robolectric:robolectric:4.3"
}
