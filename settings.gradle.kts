pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieApp"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":feature:movies")
include(":feature:favorites")
include(":feature:auth")
include(":feature:search")
include(":feature:profile")
include(":feature:watched")
include(":network")
include(":navigation")
include(":app:ai")
include(":feature:social")
include(":app:message")
include(":feature:rating")
