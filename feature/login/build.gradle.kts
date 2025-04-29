plugins {
    id("moime.convention.base")
    id("moime.convention.kmp.feature")
}

android.namespace = "team.capybara.moime.feature.login"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.webview.multiplatform)

            api(libs.kmpnotifier)
            implementation(libs.stately.common)
        }
    }
}
