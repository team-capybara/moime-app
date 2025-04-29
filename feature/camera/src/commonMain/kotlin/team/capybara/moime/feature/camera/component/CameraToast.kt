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

package team.capybara.moime.feature.meeting.camera.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moime.core.designsystem.generated.resources.ic_info_circle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.theme.Gray200
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.designsystem.theme.MoimeRed
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
internal fun CameraToast(
    state: CameraToastType
) {
    Box(
        modifier = Modifier
            .background(
                color = when (state.type) {
                    CameraToastType.Type.Normal -> Gray200
                    CameraToastType.Type.Error -> MoimeRed
                },
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = when (state.type) {
                    CameraToastType.Type.Normal -> 16.dp
                    CameraToastType.Type.Error -> 8.dp
                },
                vertical = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (state.type == CameraToastType.Type.Error) {
                Icon(
                    painterResource(MoimeRes.drawable.ic_info_circle),
                    contentDescription = null,
                    tint = Gray50,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                stringResource(state.message),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = when (state.type) {
                    CameraToastType.Type.Normal -> Gray700
                    CameraToastType.Type.Error -> Gray50
                },
                lineHeight = 20.sp,
                maxLines = 1
            )
        }
    }
}
