package team.capybara.moime.buildlogic.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import team.capybara.moime.buildlogic.convention.extension.androidLibrary
import team.capybara.moime.buildlogic.convention.extension.configureAndroid

@Suppress("unused")
class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            androidLibrary {
                configureAndroid()
            }
        }
    }
}
