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

package team.capybara.moime.feature.friend.blocklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moime.core.designsystem.generated.resources.ic_chevron_left
import moime.feature.friend.generated.resources.Res
import moime.feature.friend.generated.resources.manage_blocked_friends
import moime.feature.friend.generated.resources.people_count
import moime.feature.friend.generated.resources.unblock
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray600
import team.capybara.moime.core.designsystem.theme.MoimeRed
import team.capybara.moime.core.ui.component.MoimeSimpleTopAppBar
import team.capybara.moime.core.ui.component.PaginationColumn
import team.capybara.moime.core.ui.component.SafeAreaColumn
import team.capybara.moime.feature.friend.FriendViewModel
import team.capybara.moime.feature.friend.component.MoimeFriendBar
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
internal fun FriendBlockListScreen(
    onNavigateToBack: () -> Unit,
    onNavigateToFriendDetail: (Long) -> Unit
) {
    val friendViewModel = koinViewModel<FriendViewModel>()
    val friendState by friendViewModel.uiState.collectAsState()

    SafeAreaColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        MoimeSimpleTopAppBar(
            backIconRes = MoimeRes.drawable.ic_chevron_left,
            onBack = onNavigateToBack
        )
        FriendBlockListHeader(count = friendState.blockedFriendsCount)
        Spacer(Modifier.height(12.dp))
        PaginationColumn(
            enablePaging = friendState.blockedFriends.canRequest(),
            onPaging = { friendViewModel.loadBlockedFriends() },
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(
                start = 8.dp,
                top = 24.dp,
                end = 8.dp,
                bottom = 8.dp
            )
        ) {
            items(friendState.blockedFriends.data) { friend ->
                MoimeFriendBar(
                    friend = friend,
                    action = {
                        Button(
                            onClick = { friendViewModel.unblockFriend(friend.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Gray600,
                                contentColor = MoimeRed
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                stringResource(Res.string.unblock),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                            )
                        }
                    },
                    modifier = Modifier.clickable {
                        onNavigateToFriendDetail(friend.id)
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FriendBlockListHeader(
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.manage_blocked_friends),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Gray50
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = stringResource(Res.string.people_count, count),
            color = Gray400,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}
