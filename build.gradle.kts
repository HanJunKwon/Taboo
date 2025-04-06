// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.8.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false// this version matches your Kotlin version
    id("com.android.library") version "8.8.1" apply false
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getenv("mavenCentralUsername"))
            password.set(System.getenv("mavenCentralPassword"))
        }
    }
}