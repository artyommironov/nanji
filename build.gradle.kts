buildscript {
  repositories {
    google()
    mavenCentral()
  }

  dependencies {
    classpath(Plugins.android)
    classpath(Plugins.kotlin)
  }
}

allprojects {
  repositories {
    google()
    jcenter()
    maven("https://jitpack.io")
  }
}

val clean by tasks.creating(Delete::class) {
  delete(rootProject.buildDir)
}
