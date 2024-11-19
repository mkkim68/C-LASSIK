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
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs ("C:\\Users\\SSAFY\\Documents\\project\\S11P31A604\\Classik\\app\\unityLibrary\\libs")
        }
    }
}

rootProject.name = "Classik"
include(":app", ":unityLibrary", ":xrmanifest")
project(":unityLibrary").projectDir = file("app/unityLibrary")
project(":xrmanifest").projectDir = file("app/unityLibrary/xrmanifest.androidlib")
