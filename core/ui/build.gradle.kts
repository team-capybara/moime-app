plugins {
    id("moime.convention.base")
    id("moime.convention.kmp")
    id("moime.convention.kmp.android")
    id("moime.convention.kmp.ios")
    id("moime.convention.kmp.compose")
    id("moime.convention.kotlin.serialization")
}

android.namespace = "team.capybara.moime.core.ui"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.designsystem)
                implementation(projects.core.common)
                implementation(projects.core.model)

                implementation(libs.kotlinx.datetime)
                
                implementation(libs.navigation.compose)

                implementation(libs.filekit.core)
                implementation(libs.filekit.compose)

                implementation(libs.coil.compose)
                implementation(libs.coil.ktor)

                implementation(libs.webview.multiplatform)

                implementation(libs.koin.core)

                implementation(libs.ktor.client)
                implementation(libs.ktor.client.auth)

                implementation(libs.haze)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.koin.androidx.compose)
                implementation(libs.accompanist.permissions)
                implementation(libs.coroutines.guava)
                implementation(libs.camera.camera2)
                implementation(libs.camera.lifecycle)
                implementation(libs.camera.view)
            }
        }
    }
}
