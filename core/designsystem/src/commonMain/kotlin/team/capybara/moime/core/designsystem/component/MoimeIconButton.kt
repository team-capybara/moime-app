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

package team.capybara.moime.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.MoimeRed

@Composable
fun MoimeIconButton(
    iconRes: DrawableResource,
    tint: Color = Gray50,
    size: Dp = 24.dp,
    hasBadge: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
        ) {
            Box {
                Icon(
                    painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(size).align(Alignment.Center),
                    tint = tint
                )
                if (hasBadge) {
                    Box(
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .background(MoimeRed, CircleShape)
                            .size(8.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}
