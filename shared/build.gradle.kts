import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.skie)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kmmbridge)
    alias(libs.plugins.vanniktech.mavenPublish)
    id("maven-publish")
    id("signing")
}

kotlin {
    compilerOptions {
        // expect/actual classes are in Beta; suppress the warning project-wide
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    // Android target configured via com.android.kotlin.multiplatform.library plugin
    android {
        namespace = "com.what3words.search.wrapper.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "W3WSearchWrapper"
            isStatic = true
            export(libs.what3words.core)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            api(libs.what3words.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
        }
        androidMain.dependencies {
            implementation(libs.ktor.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.serialization.json)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    pom {
        name = "what3words search wrapper"
        description = "Search wrapper for what3words address and other 3rd party search providers"
        inceptionYear = "2026"
        url = "https://github.com/what3words/w3w-kmp-search-wrapper"
        developers {
            developer {
                id = "what3words"
                name = "what3words"
                url = "development@what3words.com"
            }
        }
        scm {
            url = "https://github.com/what3words/w3w-kmp-search-wrapper/tree/master"
            connection = "scm:git:git://github.com/what3words/w3w-kmp-search-wrapper.git"
            developerConnection = "scm:git:ssh://git@github.com/what3words/w3w-kmp-search-wrapper.git"
        }
    }
}
