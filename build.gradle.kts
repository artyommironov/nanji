buildscript {
  repositories {
    google()
    mavenCentral()
  }

  dependencies {
    // https://github.com/gradle/gradle/issues/16958
    val libs = project.extensions.getByType<VersionCatalogsExtension>()
      .named("libs") as org.gradle.accessors.dm.LibrariesForLibs
    classpath(libs.androidPlugin)
    classpath(libs.kotlinPlugin)
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
