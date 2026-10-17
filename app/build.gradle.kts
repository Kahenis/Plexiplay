plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "tv.plexiplay"
    compileSdk = 35

    defaultConfig {
        applicationId = "tv.plexiplay.app"
        minSdk = 23
        targetSdk = 35
        versionCode = 116
        versionName = "1.3.99"
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true; buildConfig = true }
    signingConfigs {
        getByName("debug") {
            storeFile = file("../signing/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.register<Exec>("verifyLocks") {
    workingDir = rootProject.projectDir
    commandLine("python3", "tools/check_locks.py")
}

tasks.named("preBuild") { dependsOn("verifyLocks") }

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:plex"))
    implementation(project(":core:player"))
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.activity.compose)
    implementation(libs.appcompat)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.ui)
    implementation(libs.okhttp)
    implementation("com.google.zxing:core:3.5.3")
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coroutines.android)
    implementation("io.github.yuroyami:libmpvkt-compose:0.3.0")
}
