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

package team.capybara.moime.feature.meeting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.feature.meeting.MeetingScreen
import team.capybara.moime.feature.meeting.MeetingViewModel

@Serializable
data class MeetingRoute(val id: Long, val title: String, val status: Meeting.Status) {
    companion object {
        val NEW = MeetingRoute(-1, "", Meeting.Status.New)
    }
}

fun NavGraphBuilder.meetingScreen(
    onRefreshMeetingList: () -> Unit,
    onNavigateToFriendDetail: (Long) -> Unit,
    onNavigateToCamera: (Long) -> Unit,
    onNavigateToBack: () -> Unit
) {
    composable<MeetingRoute> { backStackEntry ->
        MeetingScreen(
            viewModel = MeetingViewModel(backStackEntry.toRoute()),
            onNavigateToCamera = onNavigateToCamera,
            onRefreshMeetingList = onRefreshMeetingList,
            onNavigateToFriendDetail = onNavigateToFriendDetail,
            onNavigateToBack = onNavigateToBack
        )
    }
}
