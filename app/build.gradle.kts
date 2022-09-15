plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  val sdkVersion = 30
  compileSdkVersion(sdkVersion)

  buildFeatures {
    viewBinding = true
  }

  defaultConfig {
    applicationId = "com.kazufukurou.nanji"
    minSdkVersion(16)
    targetSdkVersion(sdkVersion)
    versionCode = 38
    versionName = "1.3.3"
    resConfigs("en", "ja", "ru")
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
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
  implementation("com.github.artyommironov:kprefs:1.0.0")
  implementation("com.github.artyommironov:anyadapter:1.0.0")
  implementation("com.github.artyommironov:colorpicker:1.0.0")
  implementation("androidx.core:core-ktx:1.5.0")
  implementation("androidx.appcompat:appcompat:1.3.0")
  implementation("androidx.recyclerview:recyclerview:1.2.1")
  implementation("androidx.constraintlayout:constraintlayout:2.0.4")

  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.21")
}
