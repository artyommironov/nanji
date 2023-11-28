plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  namespace = "com.kazufukurou.nanji"
  val sdkVersion = 34
  compileSdk = sdkVersion

  buildFeatures {
    viewBinding = true
  }
  defaultConfig {
    applicationId = "com.kazufukurou.nanji"
    minSdk = 21
    targetSdk = sdkVersion
    versionCode = 42
    versionName = "1.4.3"
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
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
  implementation("com.github.artyommironov:anyadapter:1.0.1")
  implementation("com.google.android.material:material:1.10.0")
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
  implementation("androidx.preference:preference:1.2.1")
  implementation("androidx.recyclerview:recyclerview:1.3.2")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")

  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.10")
  testImplementation("io.mockk:mockk:1.13.7")
}
