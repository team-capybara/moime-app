package team.capybara.moime.buildlogic.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import team.capybara.moime.buildlogic.convention.extension.kotlin
import team.capybara.moime.buildlogic.convention.extension.library
import team.capybara.moime.buildlogic.convention.extension.libs

@Suppress("unused")
class KotlinSerializationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
            kotlin {
                with(sourceSets) {
                    getByName("commonMain").apply {
                        dependencies {
                            implementation(libs.library("kotlinx-serialization-json"))
                        }
                    }
                }
            }
        }
    }
}
