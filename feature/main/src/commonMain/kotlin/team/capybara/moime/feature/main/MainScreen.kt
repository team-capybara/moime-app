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

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import team.capybara.moime.core.designsystem.component.MoimeLoading
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.model.User
import team.capybara.moime.core.ui.util.NavigationUtil.isRouteInHierarchy
import team.capybara.moime.feature.main.component.MeetingsBottomSheet
import team.capybara.moime.feature.main.component.MoimeBottomNavigationBar
import team.capybara.moime.feature.main.component.MoimeMainTopAppBar
import team.capybara.moime.feature.main.component.rememberMainTopAppBarState
import team.capybara.moime.feature.main.navigation.MainNavHost

@Composable
internal fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToMyPage: () -> Unit,
    onNavigateToFriend: (User) -> Unit,
    onNavigateToMeetingDetail: (Meeting) -> Unit,
    onNavigateToMeetingCreate: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToFriendDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var currentTab by remember {
        mutableStateOf(MainTab.entries.firstOrNull {
            currentDestination.isRouteInHierarchy(it.route::class)
        } ?: MainTab.Home)
    }

    val topAppBarState = rememberMainTopAppBarState(
        profileImageUrl = uiState.user?.profileImageUrl ?: "",
        hasUnreadNotification = uiState.hasUnreadNotification
    )

    Scaffold(
        topBar = {
            MoimeMainTopAppBar(
                state = topAppBarState,
                onClickProfile = onNavigateToMyPage,
                onClickFriend = { uiState.user?.let { onNavigateToFriend(it) } },
                onClickNotification = onNavigateToNotification
            )
        },
        content = {
            uiState.user?.let {
                Box {
                    MainNavHost(
                        navController = navController,
                        homeSubTab = topAppBarState.homeSubTab,
                        insightSubTab = topAppBarState.insightSubTab,
                        isActiveMeetingVisible = topAppBarState.isBackgroundVisible.not(),
                        onActiveMeetingVisibleChanged = {
                            topAppBarState.isBackgroundVisible = it.not()
                        },
                        onSelectMeetings = { viewModel.showMeetingsBottomSheet(it) },
                        onNavigateToMeeting = onNavigateToMeetingDetail,
                        onNavigateToFriendDetail = onNavigateToFriendDetail,
                    )
                    uiState.selectedMeetings?.let { meetings ->
                        MeetingsBottomSheet(
                            meetings = meetings,
                            onClickMeeting = onNavigateToMeetingDetail,
                            onDismissRequest = { viewModel.hideMeetingsBottomSheet() },
                        )
                    }
                }
            } ?: run {
                MoimeLoading()
            }
        },
        bottomBar = {
            MoimeBottomNavigationBar(
                currentTab = currentTab,
                onClickTab = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    currentTab = it
                    topAppBarState.updateTab(it)
                },
                onAction = onNavigateToMeetingCreate,
            )
        },
    )
}
