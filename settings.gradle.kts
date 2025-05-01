enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "moime"
include(":app")
include(":core:designsystem")
include(":core:ui")
include(":core:common")
include(":core:model")
include(":core:data")
include(":core:network")
include(":feature:splash")
include(":feature:onboarding")
include(":feature:login")
include(":feature:main")
include(":feature:home")
include(":feature:insight")
include(":feature:friend")
include(":feature:notification")
include(":feature:mypage")
include(":feature:meeting")
include(":feature:camera")
