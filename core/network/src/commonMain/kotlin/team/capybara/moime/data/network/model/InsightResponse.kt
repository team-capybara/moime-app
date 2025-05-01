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

package team.capybara.moime.data.network.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import team.capybara.moime.core.common.util.DateUtil.secondsToPeriod
import team.capybara.moime.core.common.util.DateUtil.toIsoDateFormat
import team.capybara.moime.core.model.Friend
import team.capybara.moime.core.model.InsightSummary
import team.capybara.moime.core.model.Survey

@Serializable
data class InsightSummaryResponse(
    val startDate: String,
    val endDate: String,
    val friends: List<InsightSummaryFriendResponse>,
    val moimCount: InsightSummaryMeetingsCountResponse,
    val averageMoimSeconds: Int
) {
    fun toUiModel() = InsightSummary(
        startDate = LocalDate.parse(startDate.toIsoDateFormat()),
        endDate = LocalDate.parse(endDate.toIsoDateFormat()),
        metFriends = friends.map { it.toUiModel() },
        meetingsCount = moimCount.toUiModel(),
        averagePeriod = averageMoimSeconds.secondsToPeriod()
    )
}

@Serializable
data class InsightSummaryFriendResponse(
    val id: Long,
    val profile: String
) {
    fun toUiModel() = Friend(id, "", "", profile, null, false)
}

@Serializable
data class InsightSummaryMeetingsCountResponse(
    val MONDAY: Int = 0,
    val TUESDAY: Int = 0,
    val WEDNESDAY: Int = 0,
    val THURSDAY: Int = 0,
    val FRIDAY: Int = 0,
    val SATURDAY: Int = 0,
    val SUNDAY: Int = 0
) {
    fun toUiModel() = mapOf(
        DayOfWeek.MONDAY to MONDAY,
        DayOfWeek.TUESDAY to TUESDAY,
        DayOfWeek.WEDNESDAY to WEDNESDAY,
        DayOfWeek.THURSDAY to THURSDAY,
        DayOfWeek.FRIDAY to FRIDAY,
        DayOfWeek.SATURDAY to SATURDAY,
        DayOfWeek.SUNDAY to SUNDAY
    )
}

@Serializable
data class SurveyResponse(
    val count: Int,
    val submitted: Boolean
) {
    fun toUiModel() = Survey(count, submitted)
}
