rootProject.name = "W3wkmpsearchwrapper"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        mavenCentral()
        mavenLocal()
    }
    versionCatalogs {
        create("libs") {
            from("com.what3words:android-version-catalog:2026.06.01")

            // MT-9374: 2.2.0 adds the metres-based `distanceMeters` parameter. Drop this override
            // once the catalog itself moves to 2.2.0 or later.
            version("w3w-android-design-library", "2.2.0")
        }
    }
}

include(":composeApp")
include(":shared")