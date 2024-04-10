plugins {
    application
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.sprintBootDependency)
    alias(libs.plugins.springBootKotlin)
    alias(libs.plugins.flyway)
    alias(libs.plugins.kotlinSerialization)

    id("kotlinx-serialization")
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.flywayPostgres)
    }
}

repositories {
    mavenCentral()
}

group = "com.free.tvtracker"
version = "1.0.0"

flyway {
    url = "jdbc:postgresql://localhost:5432/tvtracker2"
    user = "postgres"
    password = ""
    driver = "org.postgresql.Driver"
    baselineOnMigrate = true
    locations = arrayOf("filesystem:src/main/resources/db/migration/")
}

dependencies {
    implementation(projects.api)
    implementation(libs.springBootJdbc )
    implementation(libs.springBootJpa)
    implementation(libs.springBootStarterWeb) {
        exclude(group = "com.fasterxml.jackson.core")
        exclude(group = "com.fasterxml.jackson.datatype")
        exclude(group = "com.fasterxml.jackson.module")
    }
    implementation(libs.springBootSecurity)
    implementation(libs.kotlinReflect)
    implementation(libs.jjtw)
    implementation(libs.flywayPostgres)
    implementation(libs.postgres)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.testSpringBoot)
    testImplementation(libs.testSpringBootSecurity)
}
