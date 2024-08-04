import groovy.lang.Binding
import org.gradle.internal.impldep.org.codehaus.plexus.interpolation.reflection.ReflectionValueExtractor.evaluate

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.scijava.org/content/repositories/public/")
    }
}
rootProject.name = "AiVoiceApplication"
include(":app")
include(":module_joke")
include(":module_map")
include(":module_setting")
include(":module_voice_setting")
include(":module_weather")
include(":module_developer")
include(":module_constellation")
include(":module_app_manager")
include(":lib_base")
include(":lib_network")
include(":lib_voice")


