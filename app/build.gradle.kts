import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("moime.convention.base")
    id("moime.convention.kmp")
    id("moime.convention.kmp.compose")
    id("moime.convention.kmp.ios")
    id("moime.convention.android.application")
    id("moime.convention.google.services")
    id("moime.convention.kotlin.serialization")
}

android.namespace = "team.capybara.moime.app"

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    targets.filterIsInstance<KotlinNativeTarget>().forEach {
        it.binaries.framework {
            baseName = "app"
            isStatic = true
            export(libs.kmpnotifier)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.network)
            implementation(projects.core.model)
            implementation(projects.feature.main)
            implementation(projects.feature.home)
            implementation(projects.feature.insight)
            implementation(projects.feature.login)
            implementation(projects.feature.splash)
            implementation(projects.feature.onboarding)
            implementation(projects.feature.camera)
            implementation(projects.feature.friend)
            implementation(projects.feature.meeting)
            implementation(projects.feature.mypage)
            implementation(projects.feature.notification)

            implementation(libs.navigation.compose)
            implementation(libs.koin.core)

            implementation(libs.coil.compose)
            implementation(libs.coil.ktor)

            api(libs.kmpnotifier)
            implementation(libs.stately.common)

            implementation(libs.bundles.voyager)

            implementation(libs.haze)
            implementation(libs.filekit.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.androidx.compose)
        }
    }
}
