package team.capybara.moime.buildlogic.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import team.capybara.moime.buildlogic.convention.extension.kotlin
import team.capybara.moime.buildlogic.convention.extension.library
import team.capybara.moime.buildlogic.convention.extension.libs

@Suppress("unused")
class KmpFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("moime.convention.kmp")
                apply("moime.convention.kmp.android")
                apply("moime.convention.kmp.ios")
                apply("moime.convention.kmp.compose")
                apply("moime.convention.kotlin.serialization")
            }
            kotlin {
                with(sourceSets) {
                    getByName("commonMain").apply {
                        dependencies {
                            implementation(project(":core:ui"))
                            implementation(project(":core:common"))
                            implementation(project(":core:model"))
                            implementation(project(":core:data"))

                            implementation(libs.library("kotlinx-datetime"))
                            implementation(libs.library("coroutines-core"))

                            implementation(libs.library("navigation-compose"))
                            implementation(libs.library("viewmodel-compose"))

                            implementation(libs.findBundle("koin").get())

                            implementation(libs.library("coil-compose"))
                            implementation(libs.library("coil-ktor"))

                            implementation(libs.library("ktor-client-auth"))
                        }
                    }
                }
            }
        }
    }
}
