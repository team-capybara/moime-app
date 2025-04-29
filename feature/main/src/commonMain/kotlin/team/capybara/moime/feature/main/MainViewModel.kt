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

package team.capybara.moime.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import team.capybara.moime.core.data.repository.NotificationRepository
import team.capybara.moime.core.data.repository.UserRepository
import team.capybara.moime.core.model.Meeting

internal class MainViewModel(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = MainState()
        getUser()
        refreshUnreadNotification()
    }

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getUser()
                .onSuccess { user -> _uiState.update { _uiState.value.copy(user = user) } }
        }
    }

    fun showMeetingsBottomSheet(meetings: List<Meeting>) {
        _uiState.update { _uiState.value.copy(selectedMeetings = meetings) }
    }

    fun hideMeetingsBottomSheet() {
        _uiState.update { _uiState.value.copy(selectedMeetings = null) }
    }

    fun refreshUnreadNotification() {
        viewModelScope.launch {
            notificationRepository.hasUnreadNotification()
                .onSuccess { hasUnreadNotification ->
                    _uiState.update {
                        _uiState.value.copy(
                            hasUnreadNotification = hasUnreadNotification
                        )
                    }
                }
        }
    }
}
