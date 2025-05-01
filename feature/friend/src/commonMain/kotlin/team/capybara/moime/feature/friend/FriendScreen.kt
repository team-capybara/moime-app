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

package team.capybara.moime.feature.friend

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import moime.core.designsystem.generated.resources.ic_add
import moime.core.designsystem.generated.resources.ic_close
import moime.core.designsystem.generated.resources.ic_more
import moime.feature.friend.generated.resources.Res
import moime.feature.friend.generated.resources.add_friend
import moime.feature.friend.generated.resources.add_friend_desc
import moime.feature.friend.generated.resources.manage_blocked_friends
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.component.MoimeDialog
import team.capybara.moime.core.designsystem.component.MoimeIconButton
import team.capybara.moime.core.designsystem.theme.Gray200
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.ui.component.PaginationColumn
import team.capybara.moime.core.ui.component.SafeAreaColumn
import team.capybara.moime.feature.friend.component.FriendFindContent
import team.capybara.moime.feature.friend.component.FriendInvitation
import team.capybara.moime.feature.friend.component.FriendListContentHeader
import team.capybara.moime.feature.friend.component.MoimeFriendBar
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
internal fun FriendScreen(
    viewModel: FriendViewModel,
    onNavigateToBack: () -> Unit,
    onNavigateToFriendDetail: (Long) -> Unit,
    onNavigateToFriendBlockList: () -> Unit,
    onNavigateToMeetingCreate: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    var selectedSubTab by remember {
        mutableStateOf<FriendSubTab>(FriendSubTab.MyFriend(uiState.friendsCount))
    }

    SafeAreaColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        FriendTopAppBar(
            onClose = onNavigateToBack,
            onClickBlockList = onNavigateToFriendBlockList,
        )
        PaginationColumn(
            enablePaging =
            when (selectedSubTab) {
                is FriendSubTab.MyFriend -> {
                    uiState.searchedMyFriends?.canRequest()
                        ?: uiState.myFriends.canRequest()
                }

                is FriendSubTab.RecommendedFriend -> {
                    uiState.searchedRecommendedFriends?.canRequest()
                        ?: uiState.recommendedFriends.canRequest()
                }
            },
            onPaging = {
                when (selectedSubTab) {
                    is FriendSubTab.MyFriend -> viewModel.loadMyFriends()
                    is FriendSubTab.RecommendedFriend -> viewModel.loadRecommendedFriends()
                }
            },
            contentPadding = PaddingValues(bottom = 4.dp),
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            item {
                FriendTitle()
                Spacer(Modifier.height(36.dp))
                FriendInvitation(
                    userCode = uiState.userCode,
                    profileImageUrl = uiState.userProfileImageUrl,
                )
                Spacer(Modifier.height(30.dp))
                FriendFindContent(
                    myCode = uiState.userCode,
                    foundUser = uiState.foundUser,
                    onSearch = { viewModel.findUser(it) },
                    onAddFriend = { targetFriend ->
                        viewModel.addFriend(targetFriend) {
                            onNavigateToMeetingCreate()
                        }
                    },
                    onNavigateToFriendDetail = onNavigateToFriendDetail,
                    onDismiss = { viewModel.clearFoundUser() },
                )
                Spacer(Modifier.height(28.dp))
                FriendListContentHeader(
                    tabViews =
                    listOf(
                        FriendSubTab.MyFriend(uiState.friendsCount),
                        FriendSubTab.RecommendedFriend(),
                    ),
                    selectedTabView = selectedSubTab,
                    onTabViewChanged = { selectedSubTab = it },
                    onSearch = {
                        coroutineScope.launch {
                            when (selectedSubTab) {
                                is FriendSubTab.MyFriend ->
                                    viewModel.searchMyFriends(it)

                                is FriendSubTab.RecommendedFriend ->
                                    viewModel.searchRecommendedFriends(it)
                            }
                        }
                    },
                    onDismiss = {
                        when (selectedSubTab) {
                            is FriendSubTab.MyFriend -> viewModel.clearSearchedMyFriends()
                            is FriendSubTab.RecommendedFriend -> viewModel.clearSearchedRecommendedFriends()
                        }
                    },
                )
                Spacer(Modifier.height(20.dp))
            }
            when (selectedSubTab) {
                is FriendSubTab.MyFriend -> {
                    uiState.searchedMyFriends?.data?.let {
                        items(it) { searchedMyFriend ->
                            MoimeFriendBar(
                                friend = searchedMyFriend,
                                modifier =
                                Modifier
                                    .clickable {
                                        onNavigateToFriendDetail(searchedMyFriend.id)
                                    }.padding(start = 7.5.dp),
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    } ?: items(uiState.myFriends.data) { myFriend ->
                        MoimeFriendBar(
                            friend = myFriend,
                            modifier =
                            Modifier
                                .clickable { onNavigateToFriendDetail(myFriend.id) }
                                .padding(start = 7.5.dp),
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }

                is FriendSubTab.RecommendedFriend -> {
                    uiState.searchedRecommendedFriends?.data?.let {
                        items(it) { searchedRecommendedFriend ->
                            MoimeFriendBar(
                                friend = searchedRecommendedFriend,
                                action = {
                                    MoimeIconButton(MoimeRes.drawable.ic_add) {
                                        viewModel.addFriend(searchedRecommendedFriend) {
                                            onNavigateToMeetingCreate()
                                        }
                                    }
                                },
                                modifier =
                                Modifier
                                    .clickable {
                                        onNavigateToFriendDetail(searchedRecommendedFriend.id)
                                    }.padding(start = 7.5.dp),
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    } ?: items(uiState.recommendedFriends.data) { recommendedFriend ->
                        MoimeFriendBar(
                            friend = recommendedFriend,
                            action = {
                                MoimeIconButton(MoimeRes.drawable.ic_add) {
                                    viewModel.addFriend(recommendedFriend) {
                                        onNavigateToMeetingCreate()
                                    }
                                }
                            },
                            modifier =
                            Modifier
                                .clickable { onNavigateToFriendDetail(recommendedFriend.id) }
                                .padding(start = 7.5.dp),
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
    uiState.dialogRequest?.let {
        MoimeDialog(
            request = it,
            onDismiss = { viewModel.hideDialog() },
        )
    }
}

@Composable
private fun FriendTopAppBar(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onClickBlockList: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier =
        modifier.then(
            Modifier
                .fillMaxWidth()
                .height(56.dp),
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MoimeIconButton(MoimeRes.drawable.ic_close, onClick = onClose)
        Box {
            MoimeIconButton(MoimeRes.drawable.ic_more) { menuExpanded = true }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                shape = RoundedCornerShape(8.dp),
                containerColor = Gray200,
            ) {
                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.manage_blocked_friends),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Gray700,
                            )
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onClickBlockList()
                    },
                    modifier = Modifier.height(24.dp).width(102.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun FriendTitle(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.add_friend),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Gray50,
        )
        Text(
            text = stringResource(Res.string.add_friend_desc),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Gray50,
        )
    }
}
