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

package team.capybara.moime.data.network

import kotlinx.datetime.LocalDate
import team.capybara.moime.core.common.model.CursorData
import team.capybara.moime.core.common.model.CursorRequest
import team.capybara.moime.core.model.Friend
import team.capybara.moime.core.model.InsightSummary
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.model.Survey
import team.capybara.moime.core.model.User

interface MoimeNetworkDataSource {
    suspend fun uploadImage(meetingId: Long, image: ByteArray): Result<Unit>

    suspend fun getMyFriendsCount(): Result<Int>

    suspend fun getMyFriends(
        cursor: CursorRequest,
        nickname: String? = null
    ): Result<CursorData<Friend>>

    suspend fun getRecommendedFriends(
        cursor: CursorRequest,
        nickname: String? = null
    ): Result<CursorData<Friend>>

    suspend fun getStranger(code: String): Result<Friend>

    suspend fun getStranger(targetId: Long): Result<Friend>

    suspend fun addFriend(targetId: Long): Result<Unit>

    suspend fun getBlockedFriendsCount(): Result<Int>

    suspend fun getBlockedFriends(cursor: CursorRequest): Result<CursorData<Friend>>

    suspend fun blockFriend(targetId: Long): Result<Unit>

    suspend fun unblockFriend(targetId: Long): Result<Unit>

    suspend fun getInsightSummary(): Result<InsightSummary>

    suspend fun getSurvey(): Result<Survey>

    suspend fun postSurvey(): Result<Unit>

    suspend fun getAllMeetings(): Result<List<Meeting>>

    suspend fun getAllUpcomingMeetings(): Result<List<Meeting>>

    suspend fun getAllOngoingMeetings(): Result<List<Meeting>>

    suspend fun getAllCompletedMeetings(): Result<List<Meeting>>

    suspend fun getCompletedMeetings(cursor: CursorRequest): Result<CursorData<Meeting>>

    suspend fun getOngoingMeetings(cursor: CursorRequest): Result<CursorData<Meeting>>

    suspend fun getUpcomingMeetings(cursor: CursorRequest): Result<CursorData<Meeting>>

    suspend fun getMeetingsCount(from: LocalDate, to: LocalDate): Result<Map<LocalDate, Int>>

    suspend fun getMeetingsOfDay(date: LocalDate): Result<List<Meeting>>

    suspend fun getMeetingsWith(targetId: Long, cursor: CursorRequest): Result<CursorData<Meeting>>

    suspend fun getMeetingsCountWith(targetId: Long): Result<Int>

    suspend fun hasUnreadNotification(): Result<Boolean>

    suspend fun getUser(): Result<User>

    suspend fun login(accessToken: String): Result<Unit>

    suspend fun logout(): Result<Unit>
}
