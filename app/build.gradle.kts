plugins {
  id("com.android.application")
  kotlin("android")
}

setupDependencyUpdates()

android {
  compileSdkVersion(Sdk.compile)

  buildFeatures {
    viewBinding = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }

  defaultConfig {
    applicationId = "com.kazufukurou.nanji"
    minSdkVersion(16)
    targetSdkVersion(Sdk.target)
    versionCode = 37
    versionName = "1.3.2"
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
  implementation(Libs.kotlinStdLib)
  implementation(Libs.kprefs)
  implementation(Libs.anyAdapter)
  implementation(Libs.colorPicker)
  implementation(Libs.androidxKtx)
  implementation(Libs.androidxAppCompat)
  implementation(Libs.androidxRecyclerView)
  implementation(Libs.androidxConstraintLayout)

  testImplementation(Libs.kotlinTestJunit)
}
