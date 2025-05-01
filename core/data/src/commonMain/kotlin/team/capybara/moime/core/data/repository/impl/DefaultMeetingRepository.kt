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

package team.capybara.moime.core.data.repository.impl

import kotlinx.datetime.LocalDate
import team.capybara.moime.core.common.model.CursorRequest
import team.capybara.moime.core.data.repository.api.MeetingRepository
import team.capybara.moime.data.network.MoimeNetworkDataSource

internal class DefaultMeetingRepository(
    private val dataSource: MoimeNetworkDataSource
) : MeetingRepository {

    override suspend fun getAllMeetings() = dataSource.getAllMeetings()

    override suspend fun getAllUpcomingMeetings() = dataSource.getAllUpcomingMeetings()

    override suspend fun getAllOngoingMeetings() = dataSource.getAllOngoingMeetings()

    override suspend fun getCompletedMeetings(cursor: CursorRequest) =
        dataSource.getCompletedMeetings(cursor)

    override suspend fun getOngoingMeetings(cursor: CursorRequest) =
        dataSource.getOngoingMeetings(cursor)

    override suspend fun getAllCompletedMeetings() = dataSource.getAllCompletedMeetings()

    override suspend fun getUpcomingMeetings(cursor: CursorRequest) =
        dataSource.getUpcomingMeetings(cursor)

    override suspend fun getMeetingsCount(
        from: LocalDate,
        to: LocalDate
    ) = dataSource.getMeetingsCount(from, to)

    override suspend fun getMeetingsOfDay(date: LocalDate) = dataSource.getMeetingsOfDay(date)

    override suspend fun getMeetingsWith(
        targetId: Long,
        cursor: CursorRequest
    ) = dataSource.getMeetingsWith(targetId, cursor)

    override suspend fun getMeetingsCountWith(targetId: Long) =
        dataSource.getMeetingsCountWith(targetId)
}
