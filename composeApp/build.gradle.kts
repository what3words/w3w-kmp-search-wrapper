import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

val secretProperties = Properties().apply {
    val secretPropertiesFile = rootProject.file("secret.properties")
    if (secretPropertiesFile.exists()) {
        secretPropertiesFile.inputStream().use(::load)
    }
}

android {
    namespace = "com.what3words.search.wrapper"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.what3words.search.wrapper"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        val placesApiKey: String = secretProperties.getProperty("PLACES_API") ?: ""
        buildConfigField(
            "String",
            "PLACES_API",
            "\"$placesApiKey\""
        )

        val mapboxApiKey: String = secretProperties.getProperty("MAPBOX_API") ?: ""
        buildConfigField(
            "String",
            "MAPBOX_API",
            "\"$mapboxApiKey\""
        )

        val wrapperApiKey: String = secretProperties.getProperty("PROD_API_KEY") ?: ""

        buildConfigField(
            "String",
            "W3W_WRAPPER_API_KEY",
            "\"$wrapperApiKey\""
        )
    }
    signingConfigs {
        create("shared") {
            // storeFile is nullable; leave it null when secret.properties is absent (e.g. CI unit-test runs)
            storeFile = secretProperties.getProperty("SIGNING_STORE_FILE")
                ?.takeIf { it.isNotEmpty() }
                ?.let { file(it) }
            storePassword = secretProperties.getProperty("SIGNING_STORE_PASSWORD") ?: ""
            keyPassword = secretProperties.getProperty("SIGNING_KEY_PASSWORD") ?: ""
            keyAlias = secretProperties.getProperty("SIGNING_KEY_ALIAS") ?: ""
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("shared")
        }
        release {
            signingConfig = signingConfigs.getByName("shared")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmToolchain.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmToolchain.get().toInt())
    }
    // Sources remain in KMP-style directories; map them to standard Android source sets
    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            kotlin.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        getByName("test") {
            kotlin.srcDirs("src/androidUnitTest/kotlin")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.material.icons)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.components.resources)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(projects.shared)

    implementation(libs.w3w.android.wrapper) {
        exclude(group = "com.what3words", module = "w3w-core-android")
    }
    implementation(libs.w3w.android.design.library)

    testImplementation(libs.kotlin.test)
    debugImplementation(libs.compose.ui.tooling)
}
