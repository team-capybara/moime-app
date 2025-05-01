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

package team.capybara.moime.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val MoimeColorScheme = lightColorScheme(
    primary = Gray900,
    onPrimary = Gray50,
    secondary = Gray800,
    onSecondary = Gray50,
    tertiary = Gray700,
    onTertiary = Gray50,
    background = Gray700,
    onBackground = Gray50,
    surface = Gray800,
    onSurface = Gray50,
    surfaceContainer = Gray500,
    error = MoimeRed
)

@Composable
fun MoimeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MoimeColorScheme,
        typography = MoimeTypography(),
        content = content
    )
}
