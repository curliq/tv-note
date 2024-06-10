import org.flywaydb.gradle.task.AbstractFlywayTask

plugins {
    application
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.sprintBootDependency)
    alias(libs.plugins.springBootKotlin)
    alias(libs.plugins.flyway)
}

group = "com.free.tvtracker"
version = "1.0.0"

flyway {
    url = "jdbc:postgresql://localhost:5432/tvtracker3"
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

    testImplementation(libs.testSpringBoot)
    testImplementation(libs.testSpringBootSecurity)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks {
    withType<AbstractFlywayTask> {
        notCompatibleWithConfigurationCache("because https://github.com/flyway/flyway/issues/3550")
    }
}
