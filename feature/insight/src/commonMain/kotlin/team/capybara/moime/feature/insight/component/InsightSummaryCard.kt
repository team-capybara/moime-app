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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import moime.feature.insight.generated.resources.Res
import moime.feature.insight.generated.resources.date_period_format
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.component.BOTTOM_NAV_BAR_HEIGHT
import team.capybara.moime.core.designsystem.component.HOME_TOP_APP_BAR_HEIGHT
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray500
import team.capybara.moime.core.designsystem.theme.Gray900
import team.capybara.moime.core.designsystem.theme.MoimeGreen
import team.capybara.moime.core.model.InsightSummary
import team.capybara.moime.core.ui.compositionlocal.LocalScreenSize
import team.capybara.moime.feature.insight.InsightSummaryType

@Composable
fun InsightSummaryCard(
    summary: InsightSummary,
    type: InsightSummaryType,
    expanded: Boolean,
    onExpand: (Boolean) -> Unit,
    onNavigateToFriendDetail: (id: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val expandedTopPadding = with(density) {
        WindowInsets.statusBars.getTop(this).toDp()
    } + HOME_TOP_APP_BAR_HEIGHT + 40.dp
    val expandedBottomPadding = with(density) {
        WindowInsets.navigationBars.getBottom(this).toDp()
    } + BOTTOM_NAV_BAR_HEIGHT
    val expandedHeight = with(density) {
        LocalScreenSize.current.height.toDp()
    } - expandedTopPadding - expandedBottomPadding
    val animatedHeight = animateDpAsState(
        if (expanded) expandedHeight else INSIGHT_SUMMARY_CARD_HEIGHT,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    val animatedContainerColor = animateColorAsState(
        if (expanded) MoimeGreen else Gray500
    )

    Card(
        modifier = modifier.then(Modifier.fillMaxWidth()),
        onClick = { onExpand(expanded.not()) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = animatedContainerColor.value)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight.value)
        ) {
            InsightSummaryCardTitle(
                type = type,
                startDate = summary.startDate,
                endDate = summary.endDate,
                expanded = expanded,
                modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
            )
            when (type) {
                InsightSummaryType.Friend -> {
                    InsightSummaryCardFriend(
                        friends = summary.metFriends,
                        expanded = expanded,
                        onNavigateToFriendDetail = onNavigateToFriendDetail,
                        modifier = Modifier.align(Alignment.TopEnd),
                    )
                }

                InsightSummaryType.Meeting -> {
                    InsightSummaryCardMeeting(
                        expanded = expanded,
                        parentHeight = expandedHeight.value,
                        meetingsCount = summary.meetingsCount,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }

                InsightSummaryType.Time -> {
                    InsightSummaryCardTime(
                        expanded = expanded,
                        period = summary.averagePeriod,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightSummaryCardTitle(
    type: InsightSummaryType,
    startDate: LocalDate,
    endDate: LocalDate,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedTitleColor = animateColorAsState(
        if (expanded) Gray900 else Gray50,
        animationSpec = tween(500)
    )
    val animatedTitleSize = animateFloatAsState(
        if (expanded) 32f else 24f,
        animationSpec = tween(500)
    )
    val animatedTitleLineHeight = animateFloatAsState(
        if (expanded) 38.19f else 30f,
        animationSpec = tween(500)
    )
    val animatedPeriodTextColor = animateColorAsState(
        if (expanded) Gray900 else Gray400,
        animationSpec = tween(500)
    )
    val animatedPeriodTextSize = animateFloatAsState(
        if (expanded) 16f else 12f,
        animationSpec = tween(500)
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(type.titleRes),
            color = animatedTitleColor.value,
            fontWeight = FontWeight.Bold,
            fontSize = animatedTitleSize.value.sp,
            lineHeight = animatedTitleLineHeight.value.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(
                Res.string.date_period_format,
                startDate.monthNumber,
                startDate.dayOfMonth,
                endDate.monthNumber,
                endDate.dayOfMonth
            ),
            color = animatedPeriodTextColor.value,
            fontWeight = if (expanded) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = animatedPeriodTextSize.value.sp
        )
    }
}

val INSIGHT_SUMMARY_CARD_HEIGHT = 168.dp
