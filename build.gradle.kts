import org.gradle.api.JavaVersion.VERSION_17
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
    idea
}

group = "org.http4k"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(Http4k.bom))
    implementation(platform("dev.forkhandles:forkhandles-bom:_"))

    implementation(Http4k.core)
    implementation(Http4k.cloudnative)
    implementation(Http4k.format.jackson)
    implementation("dev.forkhandles:values4k:_")
    implementation("dev.forkhandles:bunting4k:_")

    implementation("com.google.code.gson:gson:_")
    implementation(ReactiveX.rxJava3)
    implementation(ReactiveX.rxJava3.rxKotlin)
    implementation(Square.retrofit2)
    implementation(Square.retrofit2.adapter.rxJava3)
    implementation(Square.retrofit2.converter.gson)
}

tasks {
    withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JVM_17)
        }
    }

    java {
        sourceCompatibility = VERSION_17
        targetCompatibility = VERSION_17
    }

    withType<Test> {
        useJUnitPlatform()
        jvmArgs = listOf("--enable-preview")
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}


intellij {
    version.set("2024.1.2")
    plugins.set(listOf("gradle", "java", "org.jetbrains.kotlin"))
    updateSinceUntilBuild.set(false)
}
