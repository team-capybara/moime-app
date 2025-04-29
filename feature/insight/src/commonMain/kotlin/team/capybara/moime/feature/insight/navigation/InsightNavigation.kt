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

package team.capybara.moime.feature.insight.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import team.capybara.moime.core.ui.model.Tab
import team.capybara.moime.feature.insight.InsightScreen
import team.capybara.moime.feature.insight.InsightSubTab

@Serializable
data object InsightRoute : Tab.Route()

fun NavGraphBuilder.insightScreen(
    currentSubTab: InsightSubTab,
    onNavigateToFriendDetail: (Long) -> Unit
) {
    composable<InsightRoute> {
        InsightScreen(
            currentSubTab = currentSubTab,
            onNavigateToFriendDetail = onNavigateToFriendDetail
        )
    }
}
