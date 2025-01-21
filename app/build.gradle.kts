plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0"

    kotlin("plugin.serialization") version "2.0.0"

    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"

    id("com.gradleup.shadow") version "8.3.5" // To generate full JAR

    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.google.guava:guava:30.1.1-jre")

    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    implementation("commons-validator:commons-validator:1.9.0")
}

application {
    mainClass.set("labs.devotion.ipcloudsync.MainKt")
}

kotlin {
    // Ensure consistent JVM target compatibility
    jvmToolchain(17)
}

ktlint {
    enableExperimentalRules.set(true) // Ensure `.editorconfig` rules are used
}

tasks.withType<JavaCompile> {
    targetCompatibility = "17"
    sourceCompatibility = "17"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}
