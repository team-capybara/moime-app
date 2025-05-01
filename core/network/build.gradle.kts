plugins {
    id("moime.convention.base")
    id("moime.convention.kmp")
    id("moime.convention.kmp.android")
    id("moime.convention.kmp.ios")
    id("moime.convention.kotlin.serialization")
}

android.namespace = "team.capybara.moime.core.network"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.common)

            implementation(libs.kotlinx.datetime)

            implementation(libs.bundles.ktor)
            implementation(libs.koin.core)

            implementation(libs.settings)
            implementation(libs.settings.noarg)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
