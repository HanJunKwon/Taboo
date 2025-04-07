import java.util.Properties
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    id("signing")
    id("com.vanniktech.maven.publish") version "0.29.0"
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


group = "io.github.hanjunkwon"
version = "0.0.1"

android {
    namespace = "com.kwon.taboo"
    compileSdk = 35

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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    // 프로젝트의 그룹 ID, 아티팩트 ID, 버전 설정
    coordinates("io.github.hanjunkwon", "taboo-widget", "0.0.1")

    // POM 정보 설정
    pom {
        name.set("Taboo")  // 라이브러리 이름
        description.set("UI Labrary for Android")  // 라이브러리 설명
        url.set("https://github.com/HanJunKwon/Taboo")  // 프로젝트 URL
        inceptionYear.set("2025")  // 프로젝트 시작 연도

        // 라이선스 정보 설정
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        // 개발자 정보 설정
        developers {
            developer {
                id.set("copybara")                          // 개발자 ID
                name.set("HanjunKwon")                      // 개발자 이름
                url.set("https://github.com/HanJunKwon")    // 개발자 URL
                email.set("skclaqks11@naver.com")           // 개발자 e-mail
            }
        }

        // 소스 코드 관리(SCM) 정보 설정
        scm {
            url.set("https://github.com/HanJunKwon/Taboo")
            connection.set("scm:git:https://github.com/HanJunKwon/Taboo.git")
            developerConnection.set("scm:git:https://github.com/HanJunKwon/Taboo.git")
        }
    }
}