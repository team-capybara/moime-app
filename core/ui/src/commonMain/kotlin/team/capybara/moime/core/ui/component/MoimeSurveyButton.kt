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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import moime.core.designsystem.generated.resources.ic_heart_filled
import moime.core.designsystem.generated.resources.ic_heart_outlined
import moime.core.designsystem.generated.resources.ic_logo
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.core.designsystem.theme.Gray600
import team.capybara.moime.core.designsystem.theme.Gray800
import team.capybara.moime.core.designsystem.theme.MoimeGreen
import team.capybara.moime.core.designsystem.theme.PPObjectSansFontFamily
import team.capybara.moime.core.model.Survey
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
fun MoimeSurveyButton(
    onClick: () -> Unit,
    survey: Survey,
    modifier: Modifier = Modifier
) {
    val submitted = survey.submitted
    var pressed by remember { mutableStateOf(false) }
    val animatedScale = animateFloatAsState(
        targetValue = if (pressed) 0.8f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    LaunchedEffect(survey) {
        delay(1000)
        while (pressed.not() && survey.submitted.not()) {
            repeat(2) {
                pressed = true
                delay(100)
                pressed = false
                delay(200)
            }
            delay(2000)
        }
    }

    Box(
        modifier = modifier.then(
            Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            try {
                                pressed = true
                                awaitRelease()
                            } finally {
                                pressed = false
                            }
                        },
                        onTap = {
                            onClick()
                        }
                    )
                }
                .scale(animatedScale.value)
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(MoimeRes.drawable.ic_logo),
            contentDescription = null,
            tint = if (survey.submitted) MoimeGreen else Gray800,
            modifier = Modifier.fillMaxSize()
        )
        AnimatedContent(
            targetState = survey.likeCount,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                } else {
                    slideInVertically { height -> -height } + fadeIn() togetherWith
                            slideOutVertically { height -> height } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { targetCount ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        if (submitted) {
                            MoimeRes.drawable.ic_heart_filled
                        } else {
                            MoimeRes.drawable.ic_heart_outlined
                        }
                    ),
                    contentDescription = null,
                    tint = if (submitted) Gray800 else Gray600,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = if (submitted) "$targetCount" else "?",
                    color = if (submitted) Gray800 else Gray600,
                    fontFamily = PPObjectSansFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }
        }
    }
}
