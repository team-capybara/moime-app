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

package team.capybara.moime.feature.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import moime.core.ui.generated.resources.Res
import moime.core.ui.generated.resources.from_start
import moime.core.ui.generated.resources.img_meeting_thumbnail
import moime.core.ui.generated.resources.to_complete
import moime.core.ui.generated.resources.to_start
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.common.util.DateUtil.getDdayString
import team.capybara.moime.core.common.util.DateUtil.getMonthDayString
import team.capybara.moime.core.common.util.DateUtil.getPeriodString
import team.capybara.moime.core.common.util.DateUtil.getTimeString
import team.capybara.moime.core.common.util.DateUtil.isPast
import team.capybara.moime.core.common.util.DateUtil.now
import team.capybara.moime.core.common.util.DateUtil.toCompleteTime
import team.capybara.moime.core.designsystem.component.BOTTOM_NAV_BAR_HEIGHT
import team.capybara.moime.core.designsystem.component.GrayScaleColorMatrix
import team.capybara.moime.core.designsystem.component.HOME_TOP_APP_BAR_HEIGHT
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray500
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.designsystem.theme.Gray800
import team.capybara.moime.core.designsystem.theme.MoimeGreen
import team.capybara.moime.core.designsystem.theme.PPObjectSansFontFamily
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.ui.component.MoimeProfileImageStack
import team.capybara.moime.core.ui.compositionlocal.LocalScreenSize

@Suppress("ktlint:standard:max-line-length")
@Composable
fun MoimeMeetingCard(
    meeting: Meeting,
    onClick: () -> Unit,
    isAnotherActiveMeetingCardFocusing: Boolean,
    forceDefaultHeightStyle: Boolean = false,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val isActive = meeting.isActive
    var isMeetingStarted by remember { mutableStateOf(isActive && meeting.startDateTime.isPast()) }

    val todayTopPadding =
        with(density) {
            WindowInsets.statusBars.getTop(this).toDp()
        } + HOME_TOP_APP_BAR_HEIGHT + 40.dp
    val todayBottomPadding =
        with(density) {
            WindowInsets.navigationBars.getBottom(this).toDp()
        } + BOTTOM_NAV_BAR_HEIGHT + 42.dp
    val todayHeight =
        with(density) { LocalScreenSize.current.height.toDp() } - todayTopPadding - todayBottomPadding

    val animatedHeight = animateDpAsState(
        if (isActive && !forceDefaultHeightStyle) todayHeight else MOIME_CARD_HEIGHT,
        animationSpec = tween(durationMillis = 1000)
    )
    val animatedSubtextColor = animateColorAsState(
        if (isMeetingStarted) Gray50 else Gray400,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    AnimatedContent(
        targetState = isAnotherActiveMeetingCardFocusing,
        transitionSpec = { fadeIn(tween(delayMillis = 100)).togetherWith(fadeOut()) }
    ) {
        if (it) {
            Box(
                modifier = modifier.then(
                    Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .height(animatedHeight.value)
                        .background(Color.Transparent)
                )
            )
        } else {
            Card(
                modifier = modifier.then(
                    Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .height(animatedHeight.value)
                ),
                onClick = onClick,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Gray500, contentColor = Gray50)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    meeting.thumbnailUrl?.let {
                        AnimatedContent(
                            targetState = isMeetingStarted,
                            transitionSpec = {
                                fadeIn(
                                    tween(
                                        durationMillis = 1000,
                                        easing = LinearEasing
                                    )
                                ).togetherWith(
                                    fadeOut(tween(durationMillis = 1000))
                                )
                            }
                        ) { targetState ->
                            if (targetState) {
                                AsyncImage(
                                    model = it,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                AsyncImage(
                                    model = it,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    colorFilter = ColorFilter.colorMatrix(GrayScaleColorMatrix),
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    } ?: run {
                        if (isActive && !forceDefaultHeightStyle) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 56.dp)
                            ) {
                                Spacer(Modifier.weight(141f))
                                Image(
                                    painterResource(Res.drawable.img_meeting_thumbnail),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth
                                )
                                Spacer(Modifier.weight(97f))
                            }
                        }
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isMeetingStarted,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(0x80000000),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 15.dp, horizontal = 11.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MoimeProfileImageStack(meeting.participants.map { user -> user.profileImageUrl })
                        Column(
                            modifier = Modifier.weight(1f).height(MOIME_CARD_HEIGHT - 30.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = meeting.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp,
                                lineHeight = 30.sp,
                                color = Gray50,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${meeting.startDateTime.getMonthDayString()} | ${meeting.startDateTime.getTimeString()} | ${meeting.location.name}",
                                fontWeight = FontWeight.Normal,
                                color = animatedSubtextColor.value,
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Gray700,
                                    shape = RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = meeting.startDateTime.getDdayString(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Gray50,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = isActive,
                            enter = fadeIn(tween(durationMillis = 1000, delayMillis = 1000)),
                            exit = fadeOut(tween(durationMillis = 1000, delayMillis = 1000))
                        ) {
                            if (!forceDefaultHeightStyle) {
                                TimerButton(
                                    meeting = meeting,
                                    isMeetingStarted = isMeetingStarted,
                                    onMeetingStarted = { isMeetingStarted = true },
                                    modifier = Modifier
                                        .padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerButton(
    meeting: Meeting,
    isMeetingStarted: Boolean,
    onMeetingStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val meetingStatus = meeting.status
    val meetingDateTime = if (meetingStatus == Meeting.Status.Finished) {
        (meeting.finishDateTime ?: LocalDateTime.now()).toCompleteTime()
    } else {
        meeting.startDateTime
    }
    var timeString: String by remember { mutableStateOf("00:00:00") }
    val animatedButtonColor = animateColorAsState(
        if (!isMeetingStarted) Gray50 else MoimeGreen,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )
    val animatedTextColor = animateColorAsState(
        if (!isMeetingStarted) Gray800 else Gray700,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )
    LaunchedEffect(Unit) {
        while (true) {
            if (!isMeetingStarted && meetingDateTime.isPast()) {
                onMeetingStarted()
            }
            timeString = meetingDateTime.getPeriodString()
            delay(500L)
        }
    }

    Surface(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(TIMER_BUTTON_HEIGHT)
        ),
        shape = RoundedCornerShape(40.dp),
        color = animatedButtonColor.value
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = isMeetingStarted,
                transitionSpec = {
                    fadeIn(tween(1000)).togetherWith(fadeOut(tween(1000)))
                }
            ) {
                val prefixStringRes = when {
                    it && meetingStatus == Meeting.Status.Finished -> {
                        Res.string.to_complete
                    }

                    it -> {
                        Res.string.from_start
                    }

                    else -> {
                        Res.string.to_start
                    }
                }
                Text(
                    text = stringResource(prefixStringRes),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = animatedTextColor.value
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = timeString,
                fontFamily = PPObjectSansFontFamily(),
                fontSize = 16.sp,
                color = animatedTextColor.value
            )
        }
    }
}

val MOIME_CARD_HEIGHT = 128.dp
private val TIMER_BUTTON_HEIGHT = 56.dp