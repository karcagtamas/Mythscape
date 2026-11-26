plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    api(project(":core"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.scripting.jsr223)

    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.junit)
}