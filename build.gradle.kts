buildscript {
  repositories {
    google()
    jcenter()
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
  }
}

val clean by tasks.creating(Delete::class) {
  delete(rootProject.buildDir)
}
