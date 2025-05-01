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

package team.capybara.moime.data.network.ktor

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus
import team.capybara.moime.core.common.ACCESS_TOKEN_KEY
import team.capybara.moime.core.common.model.BearerTokenStorage
import team.capybara.moime.core.common.model.CursorData
import team.capybara.moime.core.common.model.CursorRequest
import team.capybara.moime.core.common.util.DateUtil.toApiFormat
import team.capybara.moime.core.common.util.DateUtil.toIsoDateTimeFormat
import team.capybara.moime.core.model.Friend
import team.capybara.moime.core.model.InsightSummary
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.model.Survey
import team.capybara.moime.core.model.User
import team.capybara.moime.data.network.MoimeNetworkDataSource
import team.capybara.moime.data.network.model.ApiException
import team.capybara.moime.data.network.model.FriendListResponse
import team.capybara.moime.data.network.model.FriendResponse
import team.capybara.moime.data.network.model.InsightSummaryResponse
import team.capybara.moime.data.network.model.MeetingCountPerMonthResponse
import team.capybara.moime.data.network.model.MeetingCountResponse
import team.capybara.moime.data.network.model.MeetingDateResponse
import team.capybara.moime.data.network.model.MeetingResponse
import team.capybara.moime.data.network.model.SurveyResponse
import team.capybara.moime.data.network.model.UserResponse
import team.capybara.moime.data.network.model.toUser

internal class KtorMoimeNetwork(
    private val httpClient: HttpClient,
    private val settings: Settings,
    private val bearerTokenStorage: BearerTokenStorage
) : MoimeNetworkDataSource {

    override suspend fun uploadImage(meetingId: Long, image: ByteArray): Result<Unit> =
        runCatching {
            httpClient.submitFormWithBinaryData(
                url = Api.MOIMS_PHOTO(meetingId),
                formData = formData {
                    append("file", image, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpg")
                        append(
                            HttpHeaders.ContentDisposition,
                            "filename=\"$meetingId-${Clock.System.now()}.jpg\""
                        )
                    })
                }
            ).also { if (it.status.value != 200) throw ApiException(it.status) }
        }

    override suspend fun getMyFriendsCount(): Result<Int> = runCatching {
        httpClient.get(Api.FRIENDS_COUNT).body<Int>()
    }

    override suspend fun getMyFriends(
        cursor: CursorRequest,
        nickname: String?
    ): Result<CursorData<Friend>> = runCatching {
        httpClient.get(Api.FRIENDS_FOLLOWINGS) {
            url {
                with(cursor) {
                    cursorId?.let { parameters.append("cursorId", it.toString()) }
                    limit?.let { parameters.append("size", it.toString()) }
                }
                nickname?.let { parameters.append("keyword", it) }
            }
        }.body<FriendListResponse>().run {
            CursorData(
                data = data.map { it.toUiModel() },
                nextCursorId = cursorId?.cursorId,
                isLast = last
            )
        }
    }

    override suspend fun getRecommendedFriends(
        cursor: CursorRequest,
        nickname: String?
    ): Result<CursorData<Friend>> = runCatching {
        httpClient.get(Api.FRIENDS_RECOMMENDED) {
            url {
                with(cursor) {
                    cursorId?.let { parameters.append("cursorId", it.toString()) }
                    limit?.let { parameters.append("size", it.toString()) }
                }
                nickname?.let { parameters.append("keyword", it) }
            }
        }.body<FriendListResponse>().run {
            CursorData(
                data = data.map { it.toUiModel() },
                nextCursorId = cursorId?.cursorId,
                isLast = last
            )
        }
    }

    override suspend fun getStranger(code: String): Result<Friend> = runCatching {
        httpClient.get(Api.USERS_FIND_CODE(code)) {
            url { parameters.append("userCode", code) }
        }.body<FriendResponse>().toUiModel()
    }

    override suspend fun getStranger(targetId: Long): Result<Friend> = runCatching {
        httpClient.get(Api.USERS_FIND_ID(targetId)) {
            url { parameters.append("userId", targetId.toString()) }
        }.body<FriendResponse>().toUiModel()
    }

    override suspend fun addFriend(targetId: Long): Result<Unit> = runCatching {
        httpClient.post(Api.FRIENDS_ADD) {
            url { parameters.append("targetId", targetId.toString()) }
        }.also { if (it.status.value != 200) throw ApiException(it.status) }
    }

    override suspend fun getBlockedFriendsCount(): Result<Int> = runCatching {
        httpClient.get(Api.FRIENDS_BLOCKED_COUNT).body<Int>()
    }

    override suspend fun getBlockedFriends(
        cursor: CursorRequest
    ): Result<CursorData<Friend>> = runCatching {
        httpClient.get(Api.FRIENDS_BLOCKED) {
            url {
                with(cursor) {
                    cursorId?.let { parameters.append("cursorId", it.toString()) }
                    limit?.let { parameters.append("size", it.toString()) }
                }
            }
        }.body<FriendListResponse>().run {
            CursorData(
                data = data.map { it.toUiModel() },
                nextCursorId = cursorId?.cursorId,
                isLast = last
            )
        }
    }

    override suspend fun blockFriend(targetId: Long): Result<Unit> = runCatching {
        httpClient.put(Api.FRIENDS_BLOCK) {
            url { parameters.append("targetId", targetId.toString()) }
        }.also { if (it.status.value != 200) throw ApiException(it.status) }
    }

    override suspend fun unblockFriend(targetId: Long): Result<Unit> = runCatching {
        httpClient.put(Api.FRIENDS_UNBLOCK) {
            url { parameters.append("targetId", targetId.toString()) }
        }.also { if (it.status.value != 200) throw ApiException(it.status) }
    }

    override suspend fun getInsightSummary(): Result<InsightSummary> = runCatching {
        httpClient.get(Api.WEEKLY_SUMMARY).body<InsightSummaryResponse>().toUiModel()
    }

    override suspend fun getSurvey(): Result<Survey> = runCatching {
        httpClient.get(Api.SURVEY_FRIEND_STATS).body<SurveyResponse>().toUiModel()
    }

    override suspend fun postSurvey(): Result<Unit> = runCatching {
        httpClient.post(Api.SURVEY_FRIEND_STATS)
    }

    override suspend fun getAllMeetings(): Result<List<Meeting>> = runCatching {
        listOf(
            Api.MOIMS_UPCOMING,
            Api.MOIMS_TODAY,
            Api.MOIMS_COMPLETE
        ).fold<String, List<Meeting>>(emptyList()) { acc, api ->
            acc + httpClient.get(api) {
                url {
                    parameters.append("size", "100")
                }
            }.body<MeetingResponse>().data.map { it.toUiModel() }
        }.sortedBy { it.startDateTime }
    }

    override suspend fun getAllUpcomingMeetings(): Result<List<Meeting>> =
        runCatching {
            httpClient.get(Api.MOIMS_UPCOMING) {
                url {
                    parameters.append("size", DEFAULT_SIZE_OF_PAGE.toString())
                }
            }.run {
                if (status.value != 200) {
                    throw ApiException(status)
                } else {
                    body<MeetingResponse>().data.map { it.toUiModel() }
                }
            }
        }

    override suspend fun getAllOngoingMeetings(): Result<List<Meeting>> =
        runCatching {
            httpClient.get(Api.MOIMS_TODAY) {
                url {
                    parameters.append("size", DEFAULT_SIZE_OF_PAGE.toString())
                }
            }.run {
                if (status.value != 200) {
                    throw ApiException(status)
                } else {
                    body<MeetingResponse>().data.map { it.toUiModel() }
                }
            }
        }

    override suspend fun getCompletedMeetings(cursor: CursorRequest): Result<CursorData<Meeting>> =
        runCatching {
            val response = httpClient.get(Api.MOIMS_COMPLETE) {
                url {
                    with(cursor) {
                        cursorId?.let { parameters.append("cursor.cursorMoimId", it.toString()) }
                        cursorDate?.let { parameters.append("cursor.cursorDate", it.toApiFormat()) }
                        limit?.let { parameters.append("size", it.toString()) }
                    }
                }
            }
            if (response.status.value != 200) throw ApiException(response.status)
            response.body<MeetingResponse>().run {
                CursorData(
                    data = data.map { it.toUiModel() },
                    nextCursorId = cursorId?.cursorMoimId,
                    nextCursorDate = cursorId?.cursorDate?.let { LocalDateTime.parse(it.toIsoDateTimeFormat()) },
                    isLast = last
                )
            }
        }

    override suspend fun getOngoingMeetings(cursor: CursorRequest): Result<CursorData<Meeting>> =
        runCatching {
            val response = httpClient.get(Api.MOIMS_TODAY) {
                url {
                    with(cursor) {
                        cursorId?.let { parameters.append("cursor.cursorMoimId", it.toString()) }
                        cursorDate?.let { parameters.append("cursor.cursorDate", it.toApiFormat()) }
                        limit?.let { parameters.append("size", it.toString()) }
                    }
                }
            }
            if (response.status.value != 200) throw ApiException(response.status)
            response.body<MeetingResponse>().run {
                CursorData(
                    data = data.map { it.toUiModel() },
                    nextCursorId = cursorId?.cursorMoimId,
                    nextCursorDate = cursorId?.cursorDate?.let { LocalDateTime.parse(it.toIsoDateTimeFormat()) },
                    isLast = last
                )
            }
        }

    override suspend fun getAllCompletedMeetings(): Result<List<Meeting>> =
        runCatching {
            httpClient.get(Api.MOIMS_COMPLETE) {
                url {
                    parameters.append("size", DEFAULT_SIZE_OF_PAGE.toString())
                }
            }.run {
                if (status.value != 200) {
                    throw ApiException(status)
                } else {
                    body<MeetingResponse>().data.map { it.toUiModel() }
                }
            }
        }

    override suspend fun getUpcomingMeetings(cursor: CursorRequest): Result<CursorData<Meeting>> =
        runCatching {
            val response = httpClient.get(Api.MOIMS_UPCOMING) {
                url {
                    with(cursor) {
                        cursorId?.let { parameters.append("cursor.cursorMoimId", it.toString()) }
                        cursorDate?.let { parameters.append("cursor.cursorDate", it.toApiFormat()) }
                        limit?.let { parameters.append("size", it.toString()) }
                    }
                }
            }
            if (response.status.value != 200) throw ApiException(response.status)
            response.body<MeetingResponse>().run {
                CursorData(
                    data = data.map { it.toUiModel() },
                    nextCursorId = cursorId?.cursorMoimId,
                    nextCursorDate = cursorId?.cursorDate?.let { LocalDateTime.parse(it.toIsoDateTimeFormat()) },
                    isLast = last
                )
            }
        }

    override suspend fun getMeetingsCount(
        from: LocalDate,
        to: LocalDate
    ): Result<Map<LocalDate, Int>> =
        runCatching {
            val meetingsCount = mutableMapOf<LocalDate, Int>()
            var targetDate = from
            while (targetDate <= to) {
                httpClient.get(Api.MOIMS_CALENDAR) {
                    url {
                        parameters.append("year", targetDate.year.toString())
                        parameters.append("month", targetDate.monthNumber.toString())
                    }
                }.run {
                    if (status.value != 200) {
                        throw ApiException(status)
                    } else {
                        meetingsCount += body<MeetingCountResponse>().parse()
                    }
                }
                targetDate = targetDate.plus(DatePeriod(months = 1))
            }
            meetingsCount
        }

    override suspend fun getMeetingsOfDay(date: LocalDate): Result<List<Meeting>> =
        runCatching {
            httpClient.get(Api.MOIMS_DATE) {
                url {
                    parameters.append("date", date.toApiFormat())
                }
            }.run {
                if (status.value != 200) {
                    throw ApiException(status)
                } else {
                    body<MeetingDateResponse>().data.map { it.toUiModel() }
                }
            }
        }

    override suspend fun getMeetingsWith(
        targetId: Long,
        cursor: CursorRequest
    ): Result<CursorData<Meeting>> =
        runCatching {
            val response = httpClient.get(Api.MOIMS_WITH(targetId)) {
                url {
                    with(cursor) {
                        cursorId?.let { parameters.append("cursorMoimId", it.toString()) }
                        limit?.let { parameters.append("size", it.toString()) }
                    }
                }
            }
            if (response.status.value != 200) {
                throw ApiException(response.status)
            } else {
                response.body<MeetingResponse>().run {
                    CursorData(
                        data = data.map { it.toUiModel() },
                        nextCursorId = cursorId?.cursorMoimId,
                        nextCursorDate = null,
                        isLast = last
                    )
                }
            }
        }

    override suspend fun getMeetingsCountWith(targetId: Long): Result<Int> = runCatching {
        httpClient.get(Api.MOIMS_WITH_COUNT(targetId)).run {
            if (status.value != 200) {
                throw ApiException(status)
            } else {
                body<MeetingCountPerMonthResponse>().count
            }
        }
    }

    override suspend fun hasUnreadNotification(): Result<Boolean> = runCatching {
        httpClient.get(Api.NOTIFICATION_EXIST).run {
            if (status.value != 200) {
                throw ApiException(status)
            } else {
                body<Boolean>()
            }
        }
    }

    override suspend fun getUser(): Result<User> = runCatching {
        httpClient.get(Api.USERS_MY).body<UserResponse>().toUser()
    }

    override suspend fun login(accessToken: String): Result<Unit> = runCatching {
        settings.putString(ACCESS_TOKEN_KEY, accessToken)
        bearerTokenStorage.add(BearerTokens(accessToken, null))
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        settings.remove(ACCESS_TOKEN_KEY)
        bearerTokenStorage.clear()
        httpClient.authProviders.forEach {
            if (it is BearerAuthProvider) {
                it.clearToken()
            }
        }
    }

    companion object {
        private const val DEFAULT_SIZE_OF_PAGE = 20
    }
}