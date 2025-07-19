plugins {
    id("java")
    kotlin("jvm") version "2.2.0"
    id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "org.intellij.sdk"
version = "2.0.0"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2.6")
        bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.gradle")
        bundledPlugin("org.jetbrains.kotlin")
    }

    implementation(platform("org.http4k:http4k-bom:5.47.0.0"))
    implementation(platform("dev.forkhandles:forkhandles-bom:2.22.3.0"))
    implementation("org.swinglabs:swingx:1.6.1")

    implementation("org.http4k:http4k-cloudnative")
    implementation("org.http4k:http4k-format-jackson")
    implementation("org.http4k:http4k-format-jackson-yaml")
    implementation("org.http4k:http4k-multipart")
    implementation("dev.forkhandles:values4k")
    implementation("dev.forkhandles:result4k")
}

intellijPlatform {
    buildSearchableOptions = false

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
        }
    }
    pluginVerification {
        ides {
            recommended()
        }
    }
}
