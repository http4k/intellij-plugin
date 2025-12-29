plugins {
    id("java")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.intellij.platform)
    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)
}

group = "org.http4k"
version = "1.11.0"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdea(libs.versions.intellij.idea) {
            useInstaller = false
        }
        bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.gradle")
        bundledPlugin("org.jetbrains.kotlin")
    }

    implementation(platform(libs.http4k.bom))
    implementation(platform(libs.forkhandles.bom))
    implementation(libs.swingx)

    implementation(libs.http4k.cloudnative)
    implementation(libs.http4k.format.jackson)
    implementation(libs.http4k.format.jackson.yaml)
    implementation(libs.http4k.multipart)
    implementation(libs.forkhandles.values4k)
    implementation(libs.forkhandles.result4k)
}

intellijPlatform {
    buildSearchableOptions = false

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
            untilBuild = provider { null }
        }
    }
    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    publishPlugin {
        token.set(System.getenv("JETBRAINS_TOKEN"))
//        hidden.set(true)
    }
}
