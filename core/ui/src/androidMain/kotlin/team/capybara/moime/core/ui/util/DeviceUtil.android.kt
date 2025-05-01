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

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import team.capybara.moime.core.ui.compositionlocal.LocalScreenSize

@Composable
actual fun getDeviceType(): DeviceType =
    when (getDeviceWidthInDp()) {
        in 0f..599f -> DeviceType.Phone
        else -> DeviceType.Tablet
    }

@Composable
fun getDeviceWidthInDp(): Float {
    val density = LocalDensity.current
    val screenSize = LocalScreenSize.current
    return with(density) { screenSize.width.toDp().value }
}
