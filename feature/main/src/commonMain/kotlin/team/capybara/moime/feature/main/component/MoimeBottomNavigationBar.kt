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

package team.capybara.moime.feature.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.hazeChild
import moime.core.designsystem.generated.resources.ic_add
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.core.designsystem.component.BOTTOM_NAV_BAR_HEIGHT
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray800
import team.capybara.moime.core.designsystem.theme.MoimeGreen
import team.capybara.moime.core.ui.compositionlocal.LocalHazeState
import team.capybara.moime.feature.main.MainTab
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
fun MoimeBottomNavigationBar(
    currentTab: MainTab,
    onClickTab: (MainTab) -> Unit,
    onAction: () -> Unit
) {
    val hazeState = LocalHazeState.current
    Surface(
        color = BACKGROUND_COLOR,
        modifier = Modifier
            .hazeChild(
                state = hazeState,
                style = HazeDefaults.style(
                    blurRadius = 16.dp
                )
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .height(BOTTOM_NAV_BAR_HEIGHT)
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = BORDER_COLOR,
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 10.dp, bottom = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(58f))
                TabIconButton(
                    isSelected = currentTab == MainTab.Home,
                    tab = MainTab.Home,
                    onClick = { onClickTab(MainTab.Home) }
                )
                Spacer(modifier = Modifier.weight(70f))
                Surface(
                    color = MoimeGreen,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            ambientColor = MoimeGreen,
                            spotColor = MoimeGreen
                        )
                        .clickable { onAction() }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(MoimeRes.drawable.ic_add),
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFF292929),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(70f))
                TabIconButton(
                    isSelected = currentTab == MainTab.Insight,
                    tab = MainTab.Insight,
                    onClick = { onClickTab(MainTab.Insight) }
                )
                Spacer(modifier = Modifier.weight(58f))
            }
        }
    }
}

@Composable
private fun TabIconButton(
    isSelected: Boolean,
    tab: MainTab,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) =
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(tab.icon),
            contentDescription = tab.name,
            modifier = modifier.then(Modifier.size(24.dp)),
            tint = ICON_COLOR.copy(alpha = if (isSelected) 1f else 0.3f)
        )
    }

private val BACKGROUND_COLOR = Gray800.copy(alpha = 0.85f)
private val BORDER_COLOR = Gray50.copy(alpha = 0.1f)
private val ICON_COLOR = Gray50
