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

package team.capybara.moime.core.data.di

import org.koin.dsl.module
import team.capybara.moime.core.data.repository.api.CameraRepository
import team.capybara.moime.core.data.repository.api.FriendRepository
import team.capybara.moime.core.data.repository.api.InsightRepository
import team.capybara.moime.core.data.repository.api.MeetingRepository
import team.capybara.moime.core.data.repository.api.NotificationRepository
import team.capybara.moime.core.data.repository.api.UserRepository
import team.capybara.moime.core.data.repository.impl.DefaultCameraRepository
import team.capybara.moime.core.data.repository.impl.DefaultFriendRepository
import team.capybara.moime.core.data.repository.impl.DefaultInsightRepository
import team.capybara.moime.core.data.repository.impl.DefaultMeetingRepository
import team.capybara.moime.core.data.repository.impl.DefaultNotificationRepository
import team.capybara.moime.core.data.repository.impl.DefaultUserRepository

val repositoryModule = module {
    single<UserRepository> { DefaultUserRepository(get()) }
    single<MeetingRepository> { DefaultMeetingRepository(get()) }
    single<FriendRepository> { DefaultFriendRepository(get()) }
    single<CameraRepository> { DefaultCameraRepository(get()) }
    single<InsightRepository> { DefaultInsightRepository(get()) }
    single<NotificationRepository> { DefaultNotificationRepository(get()) }
}
