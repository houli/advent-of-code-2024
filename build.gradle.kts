plugins {
    kotlin("jvm") version "2.1.0"
    id("com.ncorti.ktfmt.gradle") version "0.21.0"
}

group = "io.github.houli"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("me.alllex.parsus:parsus-jvm:0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}

ktfmt {
    kotlinLangStyle()
}
