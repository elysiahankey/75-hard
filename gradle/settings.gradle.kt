pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()  // Ensures the Gradle Plugin Portal is used to find plugins
    }
    plugins {
        // Add the Hilt plugin to ensure it can be resolved
        id("dagger.hilt.android.plugin") version "2.49"  // Update to the version you want
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()  // Add Maven Central and Google repositories
    }
}

rootProject.name = "75 Hard"  // Your project name
include(":app")  // Make sure your app module is included
