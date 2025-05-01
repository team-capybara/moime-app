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

package team.capybara.moime.feature.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.model.User
import team.capybara.moime.feature.main.MainScreen
import team.capybara.moime.feature.main.MainViewModel

@Serializable
data object MainRoute

fun NavGraphBuilder.mainScreen(
    onNavigateToMyPage: () -> Unit,
    onNavigateToFriend: (User) -> Unit,
    onNavigateToMeetingDetail: (Meeting) -> Unit,
    onNavigateToMeetingCreate: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToFriendDetail: (Long) -> Unit
) {
    composable<MainRoute> {
        MainScreen(
            viewModel = koinViewModel<MainViewModel>(),
            onNavigateToMyPage = onNavigateToMyPage,
            onNavigateToFriend = onNavigateToFriend,
            onNavigateToMeetingDetail = onNavigateToMeetingDetail,
            onNavigateToMeetingCreate = onNavigateToMeetingCreate,
            onNavigateToNotification = onNavigateToNotification,
            onNavigateToFriendDetail = onNavigateToFriendDetail
        )
    }
}
