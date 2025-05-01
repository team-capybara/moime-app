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

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import team.capybara.moime.core.common.util.DateUtil.toIsoDateFormat
import team.capybara.moime.core.common.util.DateUtil.toIsoDateTimeFormat
import team.capybara.moime.core.model.Location
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.model.Participant

@Serializable
data class MeetingResponse(
    val data: List<MeetingResponseData>,
    val last: Boolean,
    val cursorId: CursorResponse?,
)

@Serializable
data class MeetingResponseData(
    val id: Long,
    val title: String,
    val startedAt: String,
    val finishedAt: String?,
    val location: LocationResponse,
    val status: String,
    val owner: ParticipantResponse,
    val participants: List<ParticipantResponse>,
    val bestPhotoUrl: String?,
) {
    fun toUiModel() =
        Meeting(
            id = id,
            title = title,
            startDateTimeString = startedAt.toIsoDateTimeFormat(),
            finishDateTimeString = finishedAt?.toIsoDateTimeFormat(),
            location = location.toUiModel(),
            status = Meeting.Status.from(status),
            participants = (listOf(owner) + participants).map { it.toUiModel() },
            thumbnailUrl = bestPhotoUrl,
        )
}

@Serializable
data class LocationResponse(
    val name: String,
    val latitude: Float,
    val longitude: Float,
) {
    fun toUiModel() =
        Location(
            name = name,
            lat = latitude,
            lng = longitude,
        )
}

@Serializable
data class ParticipantResponse(
    val userId: Long,
    val profileImageUrl: String,
) {
    fun toUiModel() =
        Participant(
            id = userId,
            profileImageUrl = profileImageUrl,
        )
}

@Serializable
data class CursorResponse(
    val cursorMoimId: Int,
    val cursorDate: String? = null,
)

@Serializable
data class MeetingCountResponse(
    val data: List<MeetingCountDataResponse>,
    val total: Int,
) {
    fun parse(): Map<LocalDate, Int> =
        data.associate {
            LocalDate.parse(it.date.toIsoDateFormat()) to it.count
        }
}

@Serializable
data class MeetingCountDataResponse(
    val date: String,
    val count: Int,
)

@Serializable
data class MeetingDateResponse(
    val data: List<MeetingResponseData>,
    val total: Int,
)

@Serializable
data class MeetingCountPerMonthResponse(
    val count: Int,
)
