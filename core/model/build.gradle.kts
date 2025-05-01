plugins {
    id("moime.convention.base")
    id("moime.convention.kmp")
    id("moime.convention.kmp.android")
    id("moime.convention.kmp.ios")
    id("moime.convention.kotlin.serialization")
}

android.namespace = "team.capybara.moime.core.model"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
