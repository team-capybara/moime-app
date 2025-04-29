plugins {
    id("moime.convention.base")
    id("moime.convention.kmp.feature")
}

android.namespace = "team.capybara.moime.feature.home"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.haze)
            implementation(libs.calendar)
            implementation(libs.pullrefresh)
        }
    }
}
