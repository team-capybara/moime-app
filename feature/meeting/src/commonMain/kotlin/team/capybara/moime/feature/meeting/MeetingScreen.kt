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

package team.capybara.moime.feature.meeting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.FriendDetailNavigationHandler
import team.capybara.moime.core.ui.jsbridge.PopHandler

@Composable
internal fun MeetingScreen(
    viewModel: MeetingViewModel,
    onNavigateToCamera: (Long) -> Unit,
    onRefreshMeetingList: () -> Unit,
    onNavigateToFriendDetail: (Long) -> Unit,
    onNavigateToBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        snapshotFlow { uiState }
            .distinctUntilChanged()
            .filter { it is MeetingState.Camera }
            .collect {
                onNavigateToCamera((it as MeetingState.Camera).meetingId)
                viewModel.reset()
            }
    }

    val popHandler = PopHandler { onNavigateToBack() }
    val friendDetailNavigationHandler = FriendDetailNavigationHandler {
        onNavigateToFriendDetail(it)
    }

    MoimeWebView(
        url = viewModel.webViewUrl,
        jsMessageHandlers = listOf(
            viewModel.CameraJsMessageHandler(),
            viewModel.imageDownloadHandler,
            popHandler,
            friendDetailNavigationHandler,
        ),
        onDispose = { onRefreshMeetingList() }
    )
}
