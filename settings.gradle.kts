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

rootProject.name = "TabooSample"
include(":app")
include(":taboo-widget")
include(":taboo-ui-core")
include(":taboo-compose-ui-widget")
include(":taboo-core")
