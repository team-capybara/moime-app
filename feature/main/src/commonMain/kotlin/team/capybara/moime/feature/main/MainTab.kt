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

package team.capybara.moime.feature.main

import moime.core.designsystem.generated.resources.ic_clipboard_text
import moime.core.designsystem.generated.resources.ic_home
import org.jetbrains.compose.resources.DrawableResource
import team.capybara.moime.core.ui.model.SubTab
import team.capybara.moime.core.ui.model.Tab
import team.capybara.moime.feature.home.HomeSubTab
import team.capybara.moime.feature.home.navigation.HomeRoute
import team.capybara.moime.feature.insight.InsightSubTab
import team.capybara.moime.feature.insight.navigation.InsightRoute
import moime.core.designsystem.generated.resources.Res as MoimeRes

enum class MainTab(
    override val route: Tab.Route,
    val icon: DrawableResource,
    val subTabs: List<SubTab>
) : Tab {
    Home(
        route = HomeRoute,
        icon = MoimeRes.drawable.ic_home,
        subTabs = HomeSubTab.entries
    ),
    Insight(
        route = InsightRoute,
        icon = MoimeRes.drawable.ic_clipboard_text,
        subTabs = InsightSubTab.entries
    );
}
