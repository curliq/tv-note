buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.flywayPostgres)
    }
}

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.springBoot) apply false
    alias(libs.plugins.sprintBootDependency) apply false
    alias(libs.plugins.springBootKotlin) apply false
    alias(libs.plugins.flyway) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.googleServicesAndroid) apply false
}
