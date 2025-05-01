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

object Api {
    const val BASE_URL = "https://api.moime.app/external/"

    private object Path {
        const val USERS = "users"
        const val MOIMS = "moims"
        const val FRIENDS = "users/friends"
        const val STAT = "stat"
        const val NOTIFICATION = "users/notifications"
        const val SURVEY = "survey"
    }

    const val USERS_MY = "${Path.USERS}/my"
    fun USERS_FIND_ID(id: Long) = "${Path.USERS}/id/$id"
    fun USERS_FIND_CODE(code: String) = "${Path.USERS}/code/$code"

    const val MOIMS_UPCOMING = "${Path.MOIMS}/upcoming"
    const val MOIMS_TODAY = "${Path.MOIMS}/today"
    const val MOIMS_COMPLETE = "${Path.MOIMS}/complete"
    fun MOIMS_PHOTO(moimId: Long) = "${Path.MOIMS}/$moimId/photos"
    const val MOIMS_CALENDAR = "${Path.MOIMS}/calendar"
    const val MOIMS_DATE = "${Path.MOIMS}/date"
    fun MOIMS_WITH(targetId: Long) = "${Path.MOIMS}/shared/$targetId"
    fun MOIMS_WITH_COUNT(targetId: Long) = "${Path.MOIMS}/shared/$targetId/count"

    const val WEEKLY_SUMMARY = "${Path.STAT}/weekly/moim"

    const val NOTIFICATION_EXIST = "${Path.NOTIFICATION}/unchecked/exist"

    const val FRIENDS_FOLLOWINGS = "${Path.FRIENDS}/followings"
    const val FRIENDS_RECOMMENDED = "${Path.FRIENDS}/followers/strangers"
    const val FRIENDS_COUNT = "${Path.FRIENDS}/followings/count"
    const val FRIENDS_ADD = Path.FRIENDS
    const val FRIENDS_BLOCK = "${Path.FRIENDS}/block"
    const val FRIENDS_UNBLOCK = "${Path.FRIENDS}/unblock"
    const val FRIENDS_BLOCKED = "${Path.FRIENDS}/blocked"
    const val FRIENDS_BLOCKED_COUNT = "${Path.FRIENDS}/blocked/count"

    const val SURVEY_FRIEND_STATS = "${Path.SURVEY}/friend-stats"
}
