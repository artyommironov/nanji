buildscript {
  repositories {
    google()
    mavenCentral()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.2.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
  }
}

val clean by tasks.creating(Delete::class) {
  delete(rootProject.buildDir)
}
