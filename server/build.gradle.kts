plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
}

group = "eu.karcags.mythscape"
version = "1.0.0"
application {
    mainClass = "eu.karcags.mythscape.ApplicationKt"
}

dependencies {
    api(project(":core"))
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.testHost)
    testImplementation(libs.kotlin.testJunit)
}