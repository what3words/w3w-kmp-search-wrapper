import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.multiplatform.library)
    alias(libs.plugins.touchlab.skie)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kmmbridge)
    alias(libs.plugins.vanniktech.maven.publish)
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
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvmToolchain.get()))
        }

        withHostTest {}
        withDeviceTest {}
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "W3WKotlinSearchWrapper"
            isStatic = true
            export(libs.w3w.core.multiplatform)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            api(libs.w3w.core.multiplatform)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
        }
        getByName("androidHostTest") {
            dependencies {
                implementation(libs.kotlin.test.junit)
            }
        }
    }
}

skie {
    features {
        enableFutureCombineExtensionPreview = true
        enableFlowCombineConvertorPreview = true
    }
    build {
        produceDistributableFramework()
    }
}

kmmbridge {
    gitHubReleaseArtifacts()
    spm(swiftToolVersion = "5.8") {
        iOS { v("14") }
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
        licenses {
            license {
                name = "MIT License"
                url = "https://github.com/what3words/w3w-kmp-search-wrapper/blob/master/LICENSE"
                distribution = "https://www.opensource.org/licenses/mit-license.php"
            }
        }
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
