package team.capybara.moime.buildlogic.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import team.capybara.moime.buildlogic.convention.extension.android
import team.capybara.moime.buildlogic.convention.extension.androidApplication
import team.capybara.moime.buildlogic.convention.extension.library
import team.capybara.moime.buildlogic.convention.extension.libs
import team.capybara.moime.buildlogic.convention.extension.version

@Suppress("unused")
class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            androidApplication {
                android {
                    namespace?.let { this.namespace = it }
                    compileSdkVersion(libs.version("compileSdk").toInt())
                    defaultConfig {
                        applicationId = "team.capybara.moime"
                        minSdk = libs.version("minSdk").toInt()
                        targetSdk = libs.version("targetSdk").toInt()
                        versionCode = libs.version("versionCode").toInt()
                        versionName = libs.version("versionName")
                    }
                    buildFeatures {
                        compose = true
                    }
                    composeOptions {
                        kotlinCompilerExtensionVersion = libs.version("compose-multiplatform")
                    }
                    packaging {
                        resources {
                            excludes += listOf(
                                "/META-INF/{AL2.0,LGPL2.1}",
                                "META-INF/INDEX.LIST"
                            )
                        }
                    }
                    buildTypes {
                        getByName("release") {
                            isMinifyEnabled = false
                        }
                    }
                    compileOptions {
                        isCoreLibraryDesugaringEnabled = true
                        sourceCompatibility = JavaVersion.VERSION_17
                        targetCompatibility = JavaVersion.VERSION_17
                    }
                }
                dependencies {
                    add("coreLibraryDesugaring", libs.library("android-desugar"))
                }
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }

                (this as CommonExtension<*, *, *, *, *, *>).lint {
                    // shell friendly
                    val filename = displayName.replace(":", "_").replace("[\\s']".toRegex(), "")

                    xmlReport = true
                    xmlOutput =
                        rootProject.layout.buildDirectory.file("lint-reports/lint-results-${filename}.xml")
                            .get().asFile

                    htmlReport = true
                    htmlOutput =
                        rootProject.layout.buildDirectory.file("lint-reports/lint-results-${filename}.html")
                            .get().asFile

                    // for now
                    sarifReport = false
                }
            }
        }
    }
}
