import java.util.Properties
import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    id("signing")
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

val signingKeyId: String? = localProperties.getProperty("signing.keyId")
val signingPassword: String? = localProperties.getProperty("signing.password")
val signingSecretKeyRingFile: String? = localProperties.getProperty("signing.secretKeyRingFile")
val ossrhUsername: String? = localProperties.getProperty("ossrhUsername")
val ossrhPassword: String? = localProperties.getProperty("ossrhPassword")


group = "com.kwon.taboo"
version = "1.0.0"

android {
    namespace = "com.kwon.taboo"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                cppFlags("")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable = true
    }
    dataBinding {
        enable = true
    }

    buildFeatures {
        buildConfig = true
    }

    publishing {
        singleVariant("release") // ✅ 이거 안 넣으면 components["release"] 못 씀!
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.kwon.taboo"
                artifactId = "taboo-widget"
                version = "1.0.0"

                pom {
                    name.set("Taboo")
                    description.set("UI Library for Android")
                    url.set("https://github.com/HanJunKwon/Taboo")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("copybara")
                            name.set("Hanjun Kwon")
                            email.set("skclaqks11@naver.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/HanJunKwon/Taboo.git")
                        developerConnection.set("scm:git:https://github.com/HanJunKwon/Taboo.git")
                        url.set("https://github.com/HanJunKwon/Taboo")
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(project(":taboo-ui-core"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    implementation("com.airbnb.android:lottie:6.3.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}