plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  namespace = "com.kazufukurou.nanji"
  val sdkVersion = 33
  compileSdk = sdkVersion

  buildFeatures {
    viewBinding = true
  }
  defaultConfig {
    applicationId = "com.kazufukurou.nanji"
    minSdk = 21
    targetSdk = sdkVersion
    versionCode = 38
    versionName = "1.3.3"
    resourceConfigurations.addAll(listOf("en", "ja", "ru"))
    vectorDrawables.useSupportLibrary = true
  }
  signingConfigs {
    create("release") {
      if (project.hasProperty("keyAlias")) {
        keyAlias = project.property("keyAlias").toString()
        keyPassword = project.property("keyPassword").toString()
        storeFile = file(project.property("storeFile").toString())
        storePassword = project.property("storePassword").toString()
      }
    }
  }
  buildTypes {
    getByName("release") {
      isShrinkResources = true
      isMinifyEnabled = true
      signingConfig = signingConfigs.getByName("release")
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.20")
  implementation("com.github.artyommironov:anyadapter:1.0.1")
  implementation("com.github.artyommironov:colorpicker:1.1.0")
  implementation("com.github.artyommironov:kprefs:1.1.0")
  implementation("com.google.android.material:material:1.7.0")
  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.5.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
  implementation("androidx.preference:preference:1.2.0")
  implementation("androidx.recyclerview:recyclerview:1.2.1")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")

  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.7.20")
}
