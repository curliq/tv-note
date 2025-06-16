import org.flywaydb.gradle.task.AbstractFlywayTask

plugins {
    application
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.sprintBootDependency)
    alias(libs.plugins.springBootKotlin)
    alias(libs.plugins.flyway)
    alias(libs.plugins.kover)
}

group = "com.free.tvtracker"
version = "10"

flyway {
    url = System.getenv("BOOT_DATABASE_URL") //jdbc:postgresql://localhost:5432/track8
    user = System.getenv("BOOT_DATABASE_USERNAME")
    password = System.getenv("BOOT_DATABASE_PASSWORD")
    driver = "org.postgresql.Driver"
    baselineOnMigrate = true
    locations = arrayOf("filesystem:src/main/resources/db/migration/")
}

dependencies {
    implementation(projects.api)
    implementation(libs.springBootJdbc )
    implementation(libs.springBootJpa)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootSecurity)
    implementation(libs.kotlinReflect)
    implementation(libs.jjtw)
    implementation(libs.flywayPostgres)
    implementation(libs.postgres)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.logback.classic)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.kotlinx.datetime)
    implementation(libs.firebase.admin)
    implementation(libs.opencsv)
    implementation(libs.sentry.spring.boot.starter.jakarta)
    implementation(libs.sentry.logback)

    testImplementation(libs.testSpringBoot)
    testImplementation(libs.testSpringBootSecurity)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.hamcrest)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks {
    withType<AbstractFlywayTask> {
        notCompatibleWithConfigurationCache("because https://github.com/flyway/flyway/issues/3550")
    }
}
