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

package team.capybara.moime.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray500

@Composable
fun MoimeProfileImageStack(
    profileImageUrls: List<String>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.then(Modifier.size(50.dp)),
    ) {
        when (profileImageUrls.size) {
            0 -> {}

            1 -> {
                MoimeProfileImage(
                    profileImageUrls[0],
                    size = 36.dp,
                    enableBorder = true,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            2 -> {
                MoimeProfileImage(
                    profileImageUrls[0],
                    size = 32.dp,
                    modifier = Modifier.align(Alignment.TopStart),
                )
                MoimeProfileImage(
                    profileImageUrls[1],
                    size = 32.dp,
                    enableBorder = true,
                    modifier = Modifier.align(Alignment.BottomEnd),
                )
            }

            3 -> {
                MoimeProfileImage(
                    profileImageUrls[0],
                    size = 28.dp,
                    enableBorder = true,
                    modifier = Modifier.align(Alignment.BottomStart),
                )
                MoimeProfileImage(
                    profileImageUrls[1],
                    size = 28.dp,
                    enableBorder = true,
                    modifier = Modifier.align(Alignment.BottomEnd),
                )
                MoimeProfileImage(
                    profileImageUrls[2],
                    size = 28.dp,
                    enableBorder = true,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }

            else -> {
                MoimeProfileImage(
                    profileImageUrls[0],
                    size = 25.dp,
                    enableBorder = true,
                    modifier = Modifier.align(Alignment.TopStart),
                )
                MoimeProfileImage(
                    profileImageUrls[1],
                    size = 25.dp,
                    enableBorder = true,
                    modifier = Modifier.align(Alignment.TopEnd),
                )
                MoimeProfileImage(
                    profileImageUrls[2],
                    size = 25.dp,
                    enableBorder = true,
                    modifier = Modifier.align(Alignment.BottomStart),
                )
                Surface(
                    modifier = Modifier.size(25.dp).align(Alignment.BottomEnd),
                    shape = CircleShape,
                    color = Color.Black,
                    border = BorderStroke(1.dp, Gray500),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "+${profileImageUrls.size - 3}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Gray50,
                        )
                    }
                }
            }
        }
    }
}
