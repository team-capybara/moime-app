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

import team.capybara.moime.core.common.model.CursorRequest
import team.capybara.moime.core.data.repository.api.FriendRepository
import team.capybara.moime.data.network.MoimeNetworkDataSource

internal class DefaultFriendRepository(
    private val dataSource: MoimeNetworkDataSource
) : FriendRepository {
    override suspend fun getMyFriendsCount() = dataSource.getMyFriendsCount()

    override suspend fun getMyFriends(
        cursor: CursorRequest,
        nickname: String?
    ) = dataSource.getMyFriends(cursor, nickname)

    override suspend fun getRecommendedFriends(
        cursor: CursorRequest,
        nickname: String?
    ) = dataSource.getRecommendedFriends(cursor, nickname)

    override suspend fun getStranger(code: String) = dataSource.getStranger(code)

    override suspend fun getStranger(targetId: Long) = dataSource.getStranger(targetId)

    override suspend fun addFriend(targetId: Long) = dataSource.addFriend(targetId)

    override suspend fun getBlockedFriendsCount() = dataSource.getBlockedFriendsCount()

    override suspend fun getBlockedFriends(cursor: CursorRequest) =
        dataSource.getBlockedFriends(cursor)

    override suspend fun blockFriend(targetId: Long): Result<Unit> =
        dataSource.blockFriend(targetId)

    override suspend fun unblockFriend(targetId: Long): Result<Unit> =
        dataSource.unblockFriend(targetId)
}
