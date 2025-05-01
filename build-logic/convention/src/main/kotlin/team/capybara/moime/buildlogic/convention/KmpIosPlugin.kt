package team.capybara.moime.buildlogic.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import team.capybara.moime.buildlogic.convention.extension.kotlin

@Suppress("unused")
class KmpIosPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            kotlin {
                iosX64()
                iosArm64()
                iosSimulatorArm64()
            }
        }
    }
}
