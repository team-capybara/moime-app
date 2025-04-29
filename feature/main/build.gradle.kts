plugins {
    id("moime.convention.base")
    id("moime.convention.kmp.feature")
}

android.namespace = "team.capybara.moime.feature.main"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.home)
            implementation(projects.feature.insight)
            implementation(libs.haze)
        }
    }
}
