plugins {
    id("moime.convention.base")
    id("moime.convention.kmp.feature")
}

android.namespace = "team.capybara.moime.feature.camera"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.moko.permissions.core)
            implementation(libs.moko.permissions.compose)

            implementation(libs.moko.geo)
            implementation(libs.moko.geo.compose)

            implementation(libs.kim)
        }
    }
}
