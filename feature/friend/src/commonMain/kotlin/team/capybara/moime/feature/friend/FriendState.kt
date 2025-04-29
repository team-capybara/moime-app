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

package team.capybara.moime.feature.friend

import team.capybara.moime.core.common.model.CursorData
import team.capybara.moime.core.designsystem.component.DialogRequest
import team.capybara.moime.core.model.Friend

internal data class FriendState(
    val userCode: String,
    val userProfileImageUrl: String,
    val friendsCount: Int = 0,
    val myFriends: CursorData<Friend> = CursorData(),
    val recommendedFriends: CursorData<Friend> = CursorData(),
    val searchedMyFriends: CursorData<Friend>? = null,
    val searchedRecommendedFriends: CursorData<Friend>? = null,
    val foundUser: Friend? = null,
    val dialogRequest: DialogRequest? = null,
    val blockedFriendsCount: Int = 0,
    val blockedFriends: CursorData<Friend> = CursorData(),
    val exception: Throwable? = null
)
