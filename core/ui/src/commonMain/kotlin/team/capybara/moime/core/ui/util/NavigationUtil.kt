/*
 * Copyright 2025 Yeojun Yoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package team.capybara.moime.core.ui.util

import androidx.core.bundle.Bundle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

object NavigationUtil {
    inline fun <reified T : Any?> serializableType(
        isNullableAllowed: Boolean = false,
        json: Json = Json,
    ) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
        override fun get(bundle: Bundle, key: String) =
            bundle.getString(key)?.let<String, T>(json::decodeFromString)

        override fun parseValue(value: String): T = json.decodeFromString(value)

        override fun serializeAsValue(value: T): String = json.encodeToString(value)

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, json.encodeToString(value))
        }
    }

    fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
        this?.hierarchy?.any {
            it.hasRoute(route)
        } ?: false
}
