// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.8.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false// this version matches your Kotlin version
    id("com.android.library") version "8.8.1" apply false
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

val localProperties = File(rootDir, "local.properties").takeIf { it.exists() }?.let {
    java.util.Properties().apply { load(it.inputStream()) }
}

localProperties?.forEach { (key, value) ->
    project.extra.set(key.toString(), value.toString())
}

println("USERNAME: " + project.findProperty("mavenCentralUsername"))
println("PASSWORD: " + project.findProperty("mavenCentralPassword"))

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getenv("mavenCentralUsername"))
            password.set(System.getenv("mavenCentralPassword"))
        }
    }
}