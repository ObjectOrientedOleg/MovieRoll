pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Movie Roll"
include("app")
include(":core:colorpalette")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:domain")
include(":core:network")
include(":core:ui")
include(":feature:home")
include(":feature:moviedetails")