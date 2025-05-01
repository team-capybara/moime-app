plugins {
    id("moime.convention.base")
    id("moime.convention.kmp")
    id("moime.convention.kmp.android")
    id("moime.convention.kmp.ios")
    id("moime.convention.kmp.compose")
}

android.namespace = "team.capybara.moime.core.designsystem"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }
    }
}

compose.resources {
    publicResClass = true
}