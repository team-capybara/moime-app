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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import moime.core.designsystem.generated.resources.ic_export
import moime.feature.friend.generated.resources.Res
import moime.feature.friend.generated.resources.app_share_content_text
import moime.feature.friend.generated.resources.cannot_share_on_ipad
import moime.feature.friend.generated.resources.invite_app
import moime.feature.friend.generated.resources.invite_app_desc
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.common.Platform
import team.capybara.moime.core.common.getPlatform
import team.capybara.moime.core.designsystem.theme.Gray500
import team.capybara.moime.core.ui.component.MoimeProfileImage
import team.capybara.moime.core.ui.compositionlocal.LocalToastHandler
import team.capybara.moime.core.ui.util.DeviceType
import team.capybara.moime.core.ui.util.ShareUtil
import team.capybara.moime.core.ui.util.getDeviceType
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
fun FriendInvitation(
    userCode: String,
    profileImageUrl: String,
    modifier: Modifier = Modifier,
) {
    val toast = LocalToastHandler.current
    val scope = rememberCoroutineScope()
    val isIpad = getPlatform() == Platform.iOS && getDeviceType() == DeviceType.Tablet

    Column(modifier = modifier.then(Modifier.fillMaxWidth())) {
        Text(
            stringResource(Res.string.invite_app),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(12.dp))
        Surface(
            color = Gray500,
            shape = RoundedCornerShape(12.dp),
            modifier =
            Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    scope.launch {
                        if (isIpad) {
                            toast.show(getString(Res.string.cannot_share_on_ipad))
                        } else {
                            ShareUtil.shareText(
                                getString(Res.string.app_share_content_text, userCode),
                            )
                        }
                    }
                },
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                MoimeProfileImage(
                    imageUrl = profileImageUrl,
                    size = 40.dp,
                )
                Spacer(Modifier.width(9.dp))
                Text(
                    text = stringResource(Res.string.invite_app_desc),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    painterResource(MoimeRes.drawable.ic_export),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
