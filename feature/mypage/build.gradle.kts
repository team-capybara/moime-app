plugins {
    id("moime.convention.base")
    id("moime.convention.kmp.feature")
}

android.namespace = "team.capybara.moime.feature.mypage"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.webview.multiplatform)

            implementation(libs.moko.permissions.core)
            implementation(libs.moko.permissions.compose)
        }
    }
}
