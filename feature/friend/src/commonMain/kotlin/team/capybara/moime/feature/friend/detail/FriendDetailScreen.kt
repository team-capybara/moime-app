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

package team.capybara.moime.feature.friend.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moime.core.designsystem.generated.resources.ic_calendar
import moime.core.designsystem.generated.resources.ic_close
import moime.core.designsystem.generated.resources.ic_timer
import moime.feature.friend.generated.resources.Res
import moime.feature.friend.generated.resources.add_friend
import moime.feature.friend.generated.resources.block
import moime.feature.friend.generated.resources.day_count
import moime.feature.friend.generated.resources.empty_friend_meetings
import moime.feature.friend.generated.resources.from_get_friend
import moime.feature.friend.generated.resources.meeting_count
import moime.feature.friend.generated.resources.meeting_count_month
import moime.feature.friend.generated.resources.meeting_current_month
import moime.feature.friend.generated.resources.profile
import moime.feature.friend.generated.resources.unblock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.common.util.DateUtil.daysUntilNow
import team.capybara.moime.core.designsystem.component.MoimeDialog
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray500
import team.capybara.moime.core.designsystem.theme.MoimeGreen
import team.capybara.moime.core.designsystem.theme.MoimeRed
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.ui.component.MoimeProfileImage
import team.capybara.moime.core.ui.component.MoimeSimpleTopAppBar
import team.capybara.moime.core.ui.component.PaginationColumn
import team.capybara.moime.core.ui.component.SafeAreaColumn
import team.capybara.moime.feature.component.MoimeMeetingCard
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
internal fun FriendDetailScreen(
    viewModel: FriendDetailViewModel,
    onNavigateToBack: () -> Unit,
    onNavigateToMeetingDetail: (Meeting) -> Unit,
    onNavigateToMeetingCreate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    SafeAreaColumn {
        PaginationColumn(
            enablePaging = uiState.meetings.canRequest(),
            onPaging = { viewModel.loadMeetings() },
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        ) {
            item {
                MoimeSimpleTopAppBar(
                    backIconRes = MoimeRes.drawable.ic_close,
                    onBack = onNavigateToBack,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.profile),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp,
                    )
                    Text(
                        text =
                        stringResource(
                            if (uiState.stranger.blocked) Res.string.unblock else Res.string.block,
                        ),
                        fontWeight = FontWeight.Normal,
                        color = MoimeRed,
                        fontSize = 16.sp,
                        modifier =
                        Modifier.clickable {
                            if (uiState.stranger.blocked) {
                                viewModel.unblock()
                            } else {
                                viewModel.block()
                            }
                        },
                    )
                }
                Spacer(Modifier.height(24.dp))
                MoimeProfileImage(
                    imageUrl = uiState.stranger.profileImageUrl,
                    size = 80.dp,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = uiState.stranger.nickname,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                )
                Spacer(Modifier.height(16.dp))
                if (uiState.stranger.friendshipDateTime == null) {
                    Button(
                        onClick = {
                            viewModel.addFriend {
                                onNavigateToMeetingCreate()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
                        colors =
                        ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = MoimeGreen,
                        ),
                    ) {
                        Text(
                            text = stringResource(Res.string.add_friend),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                        )
                    }
                } else {
                    Spacer(Modifier.height(36.dp))
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val daysUntilGetFriend = uiState.stranger.friendshipDateTime?.daysUntilNow()
                    FriendDetailCard(
                        leadingIconRes = MoimeRes.drawable.ic_calendar,
                        titleRes = Res.string.from_get_friend,
                        content = daysUntilGetFriend?.toString() ?: "--",
                        trailingStringRes = daysUntilGetFriend?.let { Res.string.day_count },
                        modifier = Modifier.weight(1f),
                    )
                    FriendDetailCard(
                        leadingIconRes = MoimeRes.drawable.ic_timer,
                        titleRes = Res.string.meeting_count_month,
                        content = uiState.meetingsTotalCount.toString(),
                        trailingStringRes = Res.string.meeting_count,
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(Res.string.meeting_current_month),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
            uiState.meetings.data.takeIf { it.isNotEmpty() }?.let { meetings ->
                items(meetings) {
                    MoimeMeetingCard(
                        meeting = it,
                        onClick = { onNavigateToMeetingDetail(it) },
                        isAnotherActiveMeetingCardFocusing = false,
                        forceDefaultHeightStyle = true,
                    )
                    Spacer(Modifier.height(8.dp))
                }
            } ?: item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.empty_friend_meetings),
                        color = Gray400,
                        modifier = Modifier.padding(vertical = 32.dp)
                    )
                }
            }
            item {
                Spacer(Modifier.height(8.dp))
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
private fun FriendDetailCard(
    leadingIconRes: DrawableResource,
    titleRes: StringResource,
    content: String,
    trailingStringRes: StringResource?,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Gray500,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.then(Modifier.height(76.dp)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(leadingIconRes),
                    contentDescription = null,
                    tint = Gray400,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = stringResource(titleRes),
                    color = Gray400,
                    fontSize = 12.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = content,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                trailingStringRes?.let {
                    Text(
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
