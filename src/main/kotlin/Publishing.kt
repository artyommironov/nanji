import com.android.build.gradle.BaseExtension
import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*

fun configurePublishing(project: Project) {
  project.apply(plugin = "maven-publish")
  project.apply(plugin = "com.jfrog.bintray")

  val libName = project.name
  val libGroup = project.group.toString()
  val libVersion = project.version.toString()
  val libSite = "https://github.com/kazufukurou/$libName"
  val libGit = "$libSite.git"
  val libTracker = "$libSite/issues"
  val libDesc = project.description

  val sourcesJar by project.tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(project.the<BaseExtension>().sourceSets["main"].java.srcDirs)
  }

  project.configure<PublishingExtension> {
    publications.create<MavenPublication>(libName) {
      groupId = libGroup
      artifactId = libName
      version = libVersion
      artifact(sourcesJar)
      artifact("${project.buildDir}/outputs/aar/$libName-release.aar")
      pom {
        packaging = "aar"
        name.set(libName)
        description.set(libDesc)
        url.set(libSite)
        licenses {
          license {
            name.set("The Apache Software License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        developers {
          developer {
            id.set("kazufukurou")
            name.set("Artyom Mironov")
            email.set("kazufukurou@gmail.com")
          }
        }
        scm {
          connection.set(libGit)
          developerConnection.set(libGit)
          url.set(libSite)
        }
        withXml {
          val dependenciesNode = asNode().appendNode("dependencies")
          project.configurations["implementation"].allDependencies.forEach {
            val dependencyNode = dependenciesNode.appendNode("dependency")
            dependencyNode.appendNode("groupId", it.group)
            dependencyNode.appendNode("artifactId", it.name)
            dependencyNode.appendNode("version", it.version)
          }
        }
      }
    }
  }

  project.configure<BintrayExtension> {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications(libName)
    publish = true
    pkg.apply {
      repo = "maven"
      name = libName
      desc = libDesc
      websiteUrl = libSite
      issueTrackerUrl = libTracker
      vcsUrl = libGit
      setLicenses("Apache-2.0")
      publicDownloadNumbers = true
    }
  }
}
