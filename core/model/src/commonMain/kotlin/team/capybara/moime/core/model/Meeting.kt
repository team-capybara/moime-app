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

package team.capybara.moime.core.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Meeting(
    val id: Long,
    val title: String,
    val startDateTimeString: String,
    val finishDateTimeString: String?,
    val status: Status,
    val location: Location,
    val participants: List<Participant>,
    val thumbnailUrl: String? = null
) {
    val isActive
        get() = status == Status.Ongoing || startDateTime.isToday && status != Status.Completed

    val startDateTime: LocalDateTime
        get() = LocalDateTime.parse(startDateTimeString)

    val finishDateTime: LocalDateTime?
        get() = finishDateTimeString?.let { LocalDateTime.parse(it) }

    enum class Status(val value: String) {
        New("NEW"),
        Created("CREATED"),
        Ongoing("ONGOING"),
        Finished("FINISHED"),
        Completed("COMPLETED"),
        Failed("FAILED"),
        Unknown("UNKNOWN")
        ;

        companion object {
            fun from(status: String) = entries.find { it.value == status } ?: Unknown
        }
    }
}

private val LocalDateTime.isToday
    get() = date == Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
