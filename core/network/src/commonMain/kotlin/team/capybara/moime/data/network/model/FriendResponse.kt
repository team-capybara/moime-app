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

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import team.capybara.moime.core.common.util.DateUtil.toIsoDateTimeFormat
import team.capybara.moime.core.model.Friend

@Serializable
data class FriendListResponse(
    val data: List<FriendResponse>,
    val last: Boolean,
    val cursorId: Cursor?
)

@Serializable
data class FriendResponse(
    val id: Long,
    val code: String,
    val nickname: String,
    val profile: String,
    val friendshipDate: String?,
    val blocked: Boolean
) {
    fun toUiModel() = Friend(
        id = id,
        code = code,
        nickname = nickname,
        profileImageUrl = profile,
        friendshipDateTime = friendshipDate?.let { LocalDateTime.parse(it.toIsoDateTimeFormat()) },
        blocked = blocked
    )
}

@Serializable
data class Cursor(
    val cursorId: Int
)
