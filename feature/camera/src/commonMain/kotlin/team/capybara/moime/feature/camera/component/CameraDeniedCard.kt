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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moime.core.designsystem.generated.resources.ic_camera_filled
import moime.feature.camera.generated.resources.Res
import moime.feature.camera.generated.resources.camera_permission_denied
import moime.feature.camera.generated.resources.camera_permission_help
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray800
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
internal fun CameraDeniedCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        color = Gray800
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(MoimeRes.drawable.ic_camera_filled),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Gray50
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.camera_permission_denied),
                fontWeight = FontWeight.Bold,
                color = Gray50,
                fontSize = 20.sp
            )
            Text(
                text = stringResource(Res.string.camera_permission_help),
                fontWeight = FontWeight.Normal,
                color = Gray400,
                fontSize = 12.sp
            )
        }
    }
}
