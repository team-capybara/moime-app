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

package team.capybara.moime.feature.mypage.jsbridge

import kotlinx.serialization.Serializable

/** Javascript Interface callback which return whether notification permission is granted. */
@Serializable
data class PermissionJsCallback(
    val granted: Boolean
)

/** Javascript Interface callback which return app version string. */
@Serializable
data class AppVersionJsCallback(
    val version: String
)
