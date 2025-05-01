plugins {
    id("moime.convention.base")
    id("moime.convention.kmp.feature")
}

android.namespace = "team.capybara.moime.feature.splash"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.webview.multiplatform)
        }
    }
}
