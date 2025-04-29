import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "team.capybara.moime.buildlogic.convention"

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

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "17"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.bundles.plugins)
}

gradlePlugin {
    plugins {
        register("base") {
            id = "moime.convention.base"
            implementationClass = "team.capybara.moime.buildlogic.convention.BaseConventionPlugin"
        }
        register("kmp") {
            id = "moime.convention.kmp"
            implementationClass = "team.capybara.moime.buildlogic.convention.KmpPlugin"
        }
        register("kmpAndroid") {
            id = "moime.convention.kmp.android"
            implementationClass = "team.capybara.moime.buildlogic.convention.KmpAndroidPlugin"
        }
        register("kmpIos") {
            id = "moime.convention.kmp.ios"
            implementationClass = "team.capybara.moime.buildlogic.convention.KmpIosPlugin"
        }
        register("kmpCompose") {
            id = "moime.convention.kmp.compose"
            implementationClass = "team.capybara.moime.buildlogic.convention.KmpComposePlugin"
        }
        register("kotlinSerialization") {
            id = "moime.convention.kotlin.serialization"
            implementationClass =
                "team.capybara.moime.buildlogic.convention.KotlinSerializationPlugin"
        }
        register("kotlinAndroid") {
            id = "moime.convention.kotlin.android"
            implementationClass = "team.capybara.moime.buildlogic.convention.KotlinAndroidPlugin"
        }
        register("androidApplication") {
            id = "moime.convention.android.application"
            implementationClass =
                "team.capybara.moime.buildlogic.convention.AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "moime.convention.android.library"
            implementationClass = "team.capybara.moime.buildlogic.convention.AndroidLibraryPlugin"
        }
        register("googleServices") {
            id = "moime.convention.google.services"
            implementationClass = "team.capybara.moime.buildlogic.convention.GoogleServicesPlugin"
        }
        register("kmpFeature") {
            id = "moime.convention.kmp.feature"
            implementationClass = "team.capybara.moime.buildlogic.convention.KmpFeaturePlugin"
        }
    }
}
