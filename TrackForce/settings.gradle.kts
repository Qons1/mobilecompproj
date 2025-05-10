pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.9.22")
            version("agp", "8.2.0")
            version("firebase-bom", "32.7.0")
            version("play-services-location", "21.0.1")
            version("lifecycle", "2.6.2")
            version("core-ktx", "1.12.0")
            version("appcompat", "1.6.1")
            version("material", "1.11.0")
            version("constraintlayout", "2.1.4")
            version("junit", "4.13.2")
            version("androidx-junit", "1.1.5")
            version("espresso-core", "3.5.1")

            library("firebase-bom", "com.google.firebase", "firebase-bom").versionRef("firebase-bom")
            library("firebase-auth", "com.google.firebase", "firebase-auth-ktx")
            library("firebase-firestore", "com.google.firebase", "firebase-firestore-ktx")
            library("play-services-location", "com.google.android.gms", "play-services-location").versionRef("play-services-location")
            library("lifecycle-runtime", "androidx.lifecycle", "lifecycle-runtime-ktx").versionRef("lifecycle")
            library("lifecycle-runtime-ktx", "androidx.lifecycle", "lifecycle-runtime-ktx").versionRef("lifecycle")
            library("androidx-core-ktx", "androidx.core", "core-ktx").versionRef("core-ktx")
            library("androidx-appcompat", "androidx.appcompat", "appcompat").versionRef("appcompat")
            library("material", "com.google.android.material", "material").versionRef("material")
            library("androidx-constraintlayout", "androidx.constraintlayout", "constraintlayout").versionRef("constraintlayout")
            library("junit", "junit", "junit").versionRef("junit")
            library("androidx-junit", "androidx.test.ext", "junit").versionRef("androidx-junit")
            library("androidx-espresso-core", "androidx.test.espresso", "espresso-core").versionRef("espresso-core")

            plugin("android-application", "com.android.application").versionRef("agp")
            plugin("kotlin-android", "org.jetbrains.kotlin.android").versionRef("kotlin")
        }
    }
}

rootProject.name = "TrackForce"
include(":app")