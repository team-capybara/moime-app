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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moime.feature.friend.generated.resources.Res
import moime.feature.friend.generated.resources.add_friend
import moime.feature.friend.generated.resources.blocked_friend
import moime.feature.friend.generated.resources.friendship_date_until
import moime.feature.friend.generated.resources.input_friend_code
import moime.feature.friend.generated.resources.it_is_me
import moime.feature.friend.generated.resources.my_friend_code
import moime.feature.friend.generated.resources.search_friend_via_code
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.common.util.DateUtil.daysUntilNow
import team.capybara.moime.core.designsystem.component.MoimeTextField
import team.capybara.moime.core.designsystem.theme.Gray200
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray500
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.model.Friend
import team.capybara.moime.core.ui.component.MoimeProfileImage

@Composable
internal fun FriendFindContent(
    myCode: String,
    foundUser: Friend?,
    onSearch: (String) -> Unit,
    onAddFriend: (Friend) -> Unit,
    onNavigateToFriendDetail: (Long) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier.then(Modifier.fillMaxWidth())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(Res.string.search_friend_via_code),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(Res.string.my_friend_code, myCode),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Gray400,
                modifier = Modifier.clickable {
                    clipboardManager.setText(buildAnnotatedString { append(myCode) })
                }
            )
        }
        Spacer(Modifier.height(18.dp))
        MoimeTextField(
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Done,
            onDone = onSearch,
            onDismiss = onDismiss,
            hintTextRes = Res.string.input_friend_code
        )
        Spacer(Modifier.height(28.dp))
        AnimatedVisibility(
            visible = foundUser != null
        ) {
            FriendFindCard(
                foundUser = foundUser,
                onClick = { onNavigateToFriendDetail(it.id) },
                onAddFriend = onAddFriend,
                myCode = myCode
            )
        }
    }
}

@Composable
private fun FriendFindCard(
    foundUser: Friend?,
    onClick: (Friend) -> Unit,
    onAddFriend: (Friend) -> Unit,
    myCode: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    foundUser?.let { if (it.code != myCode) onClick(it) }
                }
        ),
        color = Gray500,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.height(206.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            foundUser?.let {
                MoimeProfileImage(
                    imageUrl = it.profileImageUrl,
                    size = 80.dp
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = foundUser?.nickname ?: "",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Gray50
            )
            if (foundUser?.friendshipDateTime == null) {
                if (foundUser?.code == myCode) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(Res.string.it_is_me),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Gray400
                    )
                } else {
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = { foundUser?.let { onAddFriend(it) } },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gray200,
                            contentColor = Gray700
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            stringResource(Res.string.add_friend),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                        )
                    }
                }
            } else {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (foundUser.blocked) {
                        stringResource(Res.string.blocked_friend)
                    } else {
                        stringResource(
                            Res.string.friendship_date_until,
                            foundUser.friendshipDateTime!!.daysUntilNow()
                        )
                    },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Gray400
                )
            }
        }
    }
}
