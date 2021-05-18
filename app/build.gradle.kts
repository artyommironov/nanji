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
  implementation(libs.kotlinStdlib)
  implementation(libs.kprefs)
  implementation(libs.anyadapter)
  implementation(libs.colorpicker)
  implementation(libs.androidxCoreKtx)
  implementation(libs.androidxAppCompat)
  implementation(libs.androidxRecyclerView)
  implementation(libs.androidxConstraintLayout)

  testImplementation(libs.kotlinTestJunit)
}
