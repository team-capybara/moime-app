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

package team.capybara.moime.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import team.capybara.moime.core.data.repository.MeetingRepository
import team.capybara.moime.core.model.Meeting

internal class HomeViewModel(
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = HomeState()
        refreshListState()
        refreshCalendarState()
    }

    fun loadCompleteMeetings() {
        if (uiState.value.homeListState.completedMeetings.canRequest().not()) return
        viewModelScope.launch {
            _uiState.value = uiState.value.loadListCompletedMeetings()
            meetingRepository.getCompletedMeetings(uiState.value.homeListState.completedMeetings.nextRequest())
                .onSuccess {
                    _uiState.value = uiState.value.updateCompletedMeetings(it)
                    _uiState.value = uiState.value.listStateLoading(false)
                }
                .onFailure { _uiState.value = uiState.value.copy(exception = it) }
        }
    }

    private fun getOngoingMeetings() {
        viewModelScope.launch {
            meetingRepository.getAllOngoingMeetings()
                .onSuccess { _uiState.value = uiState.value.setOngoingMeetings(it) }
                .onFailure { _uiState.value = uiState.value.copy(exception = it) }
        }
    }

    private fun getUpcomingMeetings() {
        viewModelScope.launch {
            meetingRepository.getAllUpcomingMeetings()
                .onSuccess { _uiState.value = uiState.value.setUpcomingMeetings(it) }
                .onFailure { _uiState.value = uiState.value.copy(exception = it) }
        }
    }

    fun refreshListState() {
        _uiState.value = uiState.value.copy(homeListState = HomeListState())
        getUpcomingMeetings()
        getOngoingMeetings()
        loadCompleteMeetings()
    }

    fun refreshCalendarState() {
        _uiState.value = uiState.value.copy(homeCalendarState = HomeCalendarState())
        getMeetingCount()
    }

    private fun getMeetingCount(
        from: LocalDate = uiState.value.homeCalendarState.minDate,
        to: LocalDate = uiState.value.homeCalendarState.maxDate
    ) {
        viewModelScope.launch {
            meetingRepository.getMeetingsCount(from, to)
                .onSuccess { _uiState.value = uiState.value.setMeetingCount(it) }
                .onFailure { _uiState.value = uiState.value.copy(exception = it) }
        }
    }

    fun loadMeetingOfDay(date: LocalDate, callback: (List<Meeting>) -> Unit) {
        viewModelScope.launch {
            meetingRepository.getMeetingsOfDay(date)
                .onSuccess { callback(it) }
                .onFailure { _uiState.value = uiState.value.copy(exception = it) }
        }
    }

    fun clearException() {
        _uiState.value = uiState.value.copy(exception = null)
    }
}
