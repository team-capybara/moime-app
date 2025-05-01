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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import moime.core.designsystem.generated.resources.ic_profile_placeholder
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.core.designsystem.theme.Gray500
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
fun MoimeProfileImage(
    imageUrl: String,
    size: Dp? = null,
    enableBorder: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var imagePainterState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = imagePainterState !is AsyncImagePainter.State.Loading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier =
                    Modifier
                        .background(color = Color.White, shape = CircleShape)
                        .then(size?.let { Modifier.size(it) } ?: Modifier),
            )
        }
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(MoimeRes.drawable.ic_profile_placeholder),
            onSuccess = { imagePainterState = it },
            modifier =
                Modifier
                    .clip(CircleShape)
                    .then(if (enableBorder) Modifier.border(1.dp, Gray500, CircleShape) else Modifier)
                    .then(size?.let { Modifier.size(it) } ?: Modifier),
        )
    }
}
