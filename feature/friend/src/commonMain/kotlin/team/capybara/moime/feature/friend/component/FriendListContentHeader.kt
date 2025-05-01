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

package team.capybara.moime.feature.friend.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import moime.feature.friend.generated.resources.Res
import moime.feature.friend.generated.resources.search_friend_via_nickname
import team.capybara.moime.core.designsystem.component.MoimeTextField
import team.capybara.moime.core.ui.component.SubTabSegmentedButtonBar
import team.capybara.moime.feature.friend.FriendSubTab

@Composable
internal fun FriendListContentHeader(
    tabViews: List<FriendSubTab>,
    selectedTabView: FriendSubTab,
    onTabViewChanged: (FriendSubTab) -> Unit,
    onSearch: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier.then(Modifier.fillMaxWidth()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubTabSegmentedButtonBar(
            subTabs = tabViews,
            selected = selectedTabView,
            onSubTabChanged = { onTabViewChanged(it) }
        )
        Spacer(Modifier.height(12.dp))
        MoimeTextField(
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Search,
            onSearch = onSearch,
            onDismiss = onDismiss,
            hintTextRes = Res.string.search_friend_via_nickname
        )
    }
}
