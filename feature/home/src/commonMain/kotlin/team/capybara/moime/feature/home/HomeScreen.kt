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

package team.capybara.moime.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import team.capybara.moime.core.designsystem.component.MoimeLoading
import team.capybara.moime.core.model.Meeting

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel,
    currentSubTab: HomeSubTab,
    isActiveMeetingVisible: Boolean,
    onActiveMeetingVisibleChanged: (Boolean) -> Unit,
    onSelectMeetings: (List<Meeting>) -> Unit,
    onNavigateToMeeting: (Meeting) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (currentSubTab) {
        HomeSubTab.ListView ->
            if (uiState.homeListState.isMeetingInitialized) {
                HomeListView(
                    state = uiState.homeListState,
                    onRefresh = { viewModel.refreshListState() },
                    onLoadCompletedMeetings = { viewModel.loadCompleteMeetings() },
                    isActiveMeetingVisible = isActiveMeetingVisible,
                    onNavigateToMeeting = onNavigateToMeeting,
                    onActiveMeetingVisibleChanged = onActiveMeetingVisibleChanged,
                )
            } else {
                MoimeLoading()
            }

        HomeSubTab.CalendarView -> {
            HomeCalendarView(
                state = uiState.homeCalendarState,
                onDayClicked = { day ->
                    viewModel.loadMeetingOfDay(day) {
                        onSelectMeetings(it)
                    }
                },
                onRefresh = { viewModel.refreshCalendarState() },
            )
        }
    }
}
