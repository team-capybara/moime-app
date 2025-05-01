package team.capybara.moime.buildlogic.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import team.capybara.moime.buildlogic.convention.extension.android
import team.capybara.moime.buildlogic.convention.extension.configureAndroid
import team.capybara.moime.buildlogic.convention.extension.kotlin
import team.capybara.moime.buildlogic.convention.extension.libraryAndroidOptions

@Suppress("unused")
class KmpAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }
            kotlin {
                androidTarget {
                    compilations.all {
                        libraryAndroidOptions {
                            compileTaskProvider.configure {
                                compilerOptions {
                                    jvmTarget.set(JvmTarget.JVM_17)
                                }
                            }
                        }
                    }
                }
            }
            android {
                configureAndroid()
            }
        }
    }
}
