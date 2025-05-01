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

package team.capybara.moime.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray900

@Composable
fun MoimeToast(
    state: ToastState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Gray900.copy(0.87f),
    contentColor: Color = Gray50
) {
    val density = LocalDensity.current
    val bottomPadding = with(density) {
        WindowInsets.navigationBars.getBottom(this)
    } + TOAST_BOTTOM_PADDING
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    LaunchedEffect(Unit) {
        delay(TOAST_ANIMATION_DURATION_MILLIS.toLong())
        delay(state.duration)
        visibleState.targetState = false
        delay(TOAST_ANIMATION_DURATION_MILLIS.toLong())
        onDismissRequest()
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(tween(TOAST_ANIMATION_DURATION_MILLIS)),
        exit = fadeOut(tween(TOAST_ANIMATION_DURATION_MILLIS)),
        modifier = modifier.then(
            Modifier.padding(bottom = bottomPadding.dp)
        )
    ) {
        Box(
            modifier = Modifier
                .height(TOAST_HEIGHT.dp)
                .background(color = containerColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.message,
                color = contentColor,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)
            )
        }
    }
}

data class ToastState(
    val message: String = "",
    val duration: Long = 2000L
)

private const val TOAST_HEIGHT = 48
private const val TOAST_ANIMATION_DURATION_MILLIS = 300
private const val TOAST_BOTTOM_PADDING = 0