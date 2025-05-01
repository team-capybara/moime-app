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

package team.capybara.moime.feature.friend.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.feature.friend.FriendScreen
import team.capybara.moime.feature.friend.FriendViewModel
import team.capybara.moime.feature.friend.blocklist.FriendBlockListScreen
import team.capybara.moime.feature.friend.detail.FriendDetailScreen
import team.capybara.moime.feature.friend.detail.FriendDetailViewModel

@Serializable
data class FriendRoute(val userCode: String, val userProfileImageUrl: String)

@Serializable
data class FriendDetailRoute(val targetId: Long)

@Serializable
data object FriendBlockListRoute

fun NavGraphBuilder.friendScreen(
    onNavigateToBack: () -> Unit,
    onNavigateToFriendDetail: (Long) -> Unit,
    onNavigateToFriendBlockList: () -> Unit,
    onNavigateToMeetingCreate: () -> Unit,
    onNavigateToMeetingDetail: (Meeting) -> Unit
) {
    composable<FriendRoute> {
        FriendScreen(
            viewModel = koinViewModel<FriendViewModel>(),
            onNavigateToBack = onNavigateToBack,
            onNavigateToFriendDetail = onNavigateToFriendDetail,
            onNavigateToFriendBlockList = onNavigateToFriendBlockList,
            onNavigateToMeetingCreate = onNavigateToMeetingCreate
        )
    }

    composable<FriendDetailRoute> {
        FriendDetailScreen(
            viewModel = koinViewModel<FriendDetailViewModel>(),
            onNavigateToBack = onNavigateToBack,
            onNavigateToMeetingDetail = onNavigateToMeetingDetail,
            onNavigateToMeetingCreate = onNavigateToMeetingCreate
        )
    }

    composable<FriendBlockListRoute> {
        FriendBlockListScreen(
            onNavigateToBack = onNavigateToBack,
            onNavigateToFriendDetail = onNavigateToFriendDetail,
        )
    }
}
