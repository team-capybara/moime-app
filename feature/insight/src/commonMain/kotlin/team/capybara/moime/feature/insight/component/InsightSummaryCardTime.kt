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

package team.capybara.moime.feature.insight.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import kotlinx.datetime.DateTimePeriod
import team.capybara.moime.core.common.util.DateUtil.formatToString
import team.capybara.moime.core.common.util.DateUtil.toSeconds
import team.capybara.moime.core.designsystem.theme.Gray600
import team.capybara.moime.core.designsystem.theme.Gray800
import team.capybara.moime.core.ui.compositionlocal.LocalScreenSize

@Composable
fun InsightSummaryCardTime(
    period: DateTimePeriod,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val screenSize = LocalScreenSize.current
    val expandedWidth = with(density) { screenSize.width.toDp() - 64.dp }
    val animatedWidth = animateDpAsState(
        if (expanded) expandedWidth else 132.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val expandedProgress = period.toSeconds() / DateTimePeriod(hours = 25).toSeconds().toFloat()
    val animatedProgress = animateFloatAsState(
        if (expanded) expandedProgress * 24 / 25 else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow
        )
    )
    val defaultXPadding = with(density) { 16.dp.toPx() }
    val pxToMoveDown = with(density) { 156.dp.toPx() }
    val animatedOffset = animateOffsetAsState(
        if (expanded) {
            Offset(-defaultXPadding, pxToMoveDown)
        } else {
            Offset(-defaultXPadding, defaultXPadding)
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = modifier.then(Modifier
            .width(animatedWidth.value)
            .height(animatedWidth.value)
            .offset { animatedOffset.value.round() }),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = animatedProgress::value,
            strokeWidth = 16.dp,
            color = Gray800,
            trackColor = Gray800.copy(alpha = 0.2f),
            strokeCap = StrokeCap.Round,
            modifier = Modifier.fillMaxSize()
        )
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = period.formatToString(),
                color = Gray800,
                fontWeight = FontWeight.SemiBold,
                fontSize = 48.sp
            )
        }
        AnimatedVisibility(
            visible = expanded.not(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Gray600, shape = CircleShape)
            )
        }
    }
}
