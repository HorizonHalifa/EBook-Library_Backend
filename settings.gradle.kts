rootProject.name = "ebooklibrarybackend"

pluginManagement {
    repositories {
        mavenCentral() // Required for jjwt
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral() // Ensures Gradle downloads dependencies
    }
}
