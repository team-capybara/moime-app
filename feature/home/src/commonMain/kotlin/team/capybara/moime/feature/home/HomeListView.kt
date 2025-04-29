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

package team.capybara.moime.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.haze
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.PullRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import moime.core.designsystem.generated.resources.ic_chevron_down
import moime.core.designsystem.generated.resources.ic_chevron_up
import moime.feature.home.generated.resources.Res
import moime.feature.home.generated.resources.empty_meetings
import moime.feature.home.generated.resources.empty_meetings_desc
import moime.feature.home.generated.resources.img_empty_meetings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.component.BOTTOM_NAV_BAR_HEIGHT
import team.capybara.moime.core.designsystem.component.HOME_TOP_APP_BAR_HEIGHT
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray500
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.ui.component.PaginationColumn
import team.capybara.moime.core.ui.compositionlocal.LocalHazeState
import team.capybara.moime.core.ui.compositionlocal.LocalScreenSize
import team.capybara.moime.feature.component.MOIME_CARD_HEIGHT
import team.capybara.moime.feature.component.MoimeMeetingCard
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
internal fun HomeListView(
    state: HomeListState,
    onNavigateToMeeting: (Meeting) -> Unit,
    onRefresh: () -> Unit,
    onLoadCompletedMeetings: () -> Unit,
    isActiveMeetingVisible: Boolean,
    onActiveMeetingVisibleChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hazeState = LocalHazeState.current
    val density = LocalDensity.current
    val screenSize = LocalScreenSize.current
    val statusBarHeight = with(density) {
        WindowInsets.statusBars.getTop(this).toDp()
    }
    val systemBarHeightInPixels = with(density) {
        WindowInsets.statusBars.getTop(this) + WindowInsets.navigationBars.getBottom(this)
    }
    val emptyCardHeight = with(density) {
        (screenSize.height -
                systemBarHeightInPixels -
                BOTTOM_NAV_BAR_HEIGHT.roundToPx() - HOME_TOP_APP_BAR_HEIGHT.roundToPx() -
                (16 * 2).dp.roundToPx()
                ).toDp()
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = onRefresh
    )
    val listState = rememberLazyListState()
    val firstVisibleItemScrollOffset = if (state.initialVisibleMeetingIndex == 0) {
        0
    } else {
        with(density) {
            (MOIME_CARD_HEIGHT.times(
                state.initialVisibleMeetingIndex
            ) + 8.dp.times(state.initialVisibleMeetingIndex - 3)).roundToPx()
        }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(0, firstVisibleItemScrollOffset)
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        val index = listState.firstVisibleItemIndex
        if (state.meetings.size > index) {
            onActiveMeetingVisibleChanged(state.meetings[index].isActive)
        }
    }

    PullRefreshLayout(
        modifier = modifier.then(Modifier.fillMaxSize()),
        state = pullRefreshState,
        indicator = {
            PullRefreshIndicator(
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = statusBarHeight + HOME_TOP_APP_BAR_HEIGHT)
            )
        }
    ) {
        AnimatedVisibility(
            visible = isActiveMeetingVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush)
            ) {
                if (listState.firstVisibleItemIndex != 0) {
                    Icon(
                        painter = painterResource(MoimeRes.drawable.ic_chevron_up),
                        contentDescription = null,
                        tint = Gray50,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(top = HOME_TOP_APP_BAR_HEIGHT + LIST_ITEM_SPACING)
                            .size(24.dp)
                            .align(Alignment.TopCenter)
                    )
                }
                if (listState.firstVisibleItemIndex != state.meetings.lastIndex) {
                    Icon(
                        painter = painterResource(MoimeRes.drawable.ic_chevron_down),
                        contentDescription = null,
                        tint = Gray50,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(bottom = BOTTOM_NAV_BAR_HEIGHT + LIST_ITEM_SPACING)
                            .size(24.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
        PaginationColumn(
            enablePaging = state.completedMeetings.canRequest(),
            onPaging = onLoadCompletedMeetings,
            modifier = Modifier.fillMaxSize().haze(state = hazeState),
            state = listState,
            contentPadding = PaddingValues(
                top = HOME_TOP_APP_BAR_HEIGHT + LIST_ITEM_SPACING,
                bottom = BOTTOM_NAV_BAR_HEIGHT + LIST_ITEM_SPACING,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(LIST_ITEM_SPACING, Alignment.Top)
        ) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            }
            if (state.meetings.isNotEmpty()) {
                items(count = state.meetings.size) {
                    MoimeMeetingCard(
                        meeting = state.meetings[it],
                        onClick = { onNavigateToMeeting(state.meetings[it]) },
                        isAnotherActiveMeetingCardFocusing = run {
                            val currentScrollIndex = listState.firstVisibleItemIndex
                            state.meetings[currentScrollIndex].isActive && it != currentScrollIndex
                        }
                    )
                }
            } else if (state.isLoading.not()) {
                item {
                    Column(
                        modifier = Modifier
                            .background(color = Gray500, shape = RoundedCornerShape(20.dp))
                            .fillMaxWidth()
                            .height(emptyCardHeight)
                            .padding(horizontal = 35.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.img_empty_meetings),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(23.dp))
                        Text(
                            stringResource(Res.string.empty_meetings),
                            color = Gray50,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            stringResource(Res.string.empty_meetings_desc),
                            color = Gray400,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }
}

private val colorStops = arrayOf(
    0.0f to Color(0xCC25FF89),
    0.67f to Color(0x0000E8BE),
)
private val gradientBrush = Brush.verticalGradient(colorStops = colorStops)

private val LIST_ITEM_SPACING = 8.dp