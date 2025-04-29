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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import team.capybara.moime.core.ui.model.SubTab
import team.capybara.moime.feature.home.HomeSubTab
import team.capybara.moime.feature.insight.InsightSubTab
import team.capybara.moime.feature.main.MainTab

@Stable
class MainTopAppBarState(
    initialTab: MainTab,
    initialHomeSubTab: HomeSubTab,
    initialInsightSubTab: InsightSubTab,
    isBackgroundVisible: Boolean,
    profileImageUrl: String,
    hasUnreadNotification: Boolean
) {
    var tab: MainTab by mutableStateOf(initialTab)
        private set

    var homeSubTab: HomeSubTab by mutableStateOf(initialHomeSubTab)
        private set

    var insightSubTab: InsightSubTab by mutableStateOf(initialInsightSubTab)
        private set

    var isBackgroundVisible: Boolean by mutableStateOf(isBackgroundVisible)

    var profileImageUrl: String by mutableStateOf(profileImageUrl)

    var hasUnreadNotification: Boolean by mutableStateOf(hasUnreadNotification)

    val currentSubTab: SubTab
        get() = when (tab) {
            MainTab.Home -> homeSubTab
            MainTab.Insight -> insightSubTab
        }

    fun updateTab(tab: MainTab) {
        this.tab = tab
    }

    fun updateSubTab(tabView: SubTab) {
        when (tabView) {
            is HomeSubTab -> homeSubTab = tabView
            is InsightSubTab -> insightSubTab = tabView
            else -> {}
        }
    }
}

@Composable
fun rememberMainTopAppBarState(
    initialTab: MainTab = MainTab.Home,
    initialHomeSubTab: HomeSubTab = HomeSubTab.ListView,
    initialInsightSubTab: InsightSubTab = InsightSubTab.Summary,
    isBackgroundVisible: Boolean = true,
    profileImageUrl: String = "",
    hasUnreadNotification: Boolean = false
): MainTopAppBarState = remember(
    initialTab,
    initialHomeSubTab,
    initialInsightSubTab,
    isBackgroundVisible,
    profileImageUrl,
    hasUnreadNotification
) {
    MainTopAppBarState(
        initialTab,
        initialHomeSubTab,
        initialInsightSubTab,
        isBackgroundVisible,
        profileImageUrl,
        hasUnreadNotification
    )
}
