import org.flywaydb.gradle.task.AbstractFlywayTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.googleServicesAndroid)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            freeCompilerArgs += listOf(
                "-Xbinary=packageId=com.yourapp",
                "-Xgenerator=DEBUG"
            )
        }
    }
    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.accompanist.navigation.material)
            implementation(compose.uiTooling)
            implementation(compose.preview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.slf4j.android)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.sqldelight.android.driver)
            implementation(libs.firebase.fcm.android)
            implementation(libs.firebase.fcm.android.ktx)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.appcompat)
            implementation(libs.posthog.android)
            implementation(libs.billing)
            implementation(libs.billing.ktx)
            implementation(libs.sqlite.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
        }
        commonMain.dependencies {
            implementation(projects.api)
            implementation(compose.runtime)
            implementation(libs.compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.animation)
            implementation(compose.material3)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.koin.core)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.uuid)
            implementation(libs.kotlinx.datetime)
            implementation(libs.landscapist.coil3)
            implementation(libs.sentry.kotlin.multiplatform)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(libs.sqlite.driver)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.mockk)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.koin.test)
            implementation(libs.turbine)
        }
    }
}

android {
    android.buildFeatures.buildConfig = true
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    namespace = "com.free.tvtracker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.free.tvtracker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 12
        versionName = "1.8"
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "ANDROID_KEY_POSTHOG", "\"${System.getenv("ANDROID_KEY_POSTHOG")}\"")
        buildConfigField("String", "KEY_DSN_SENTRY", "\"${System.getenv("ANDROID_KEY_DSN_SENTRY")}\"")
        buildConfigField("String", "SERVER_URL", "\"${System.getenv("ANDROID_SERVER_URL")}\"")
        buildConfigField("String", "SERVER_PORT", "\"${System.getenv("ANDROID_SERVER_PORT")}\"")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/INDEX.LIST"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.experimental {
    web.application {}
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.free.tvtracker.shared.db"
        dialect = "sqlite:3.24"
    }
}

tasks {
    withType<AbstractFlywayTask> {
        notCompatibleWithConfigurationCache("because https://github.com/flyway/flyway/issues/3550")
    }
}
