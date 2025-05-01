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

package team.capybara.moime.feature.insight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import team.capybara.moime.core.data.repository.api.InsightRepository

class InsightViewModel(
    private val insightRepository: InsightRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<InsightState>(InsightState.Init)
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = InsightState.Init
        getInsightSummary()
    }

    private fun getInsightSummary() {
        viewModelScope.launch {
            _uiState.value = InsightState.Loading
            val summary = insightRepository.getInsightSummary().getOrElse {
                _uiState.value = InsightState.Failure(it)
                null
            }
            val survey = insightRepository.getSurvey().getOrElse {
                _uiState.value = InsightState.Failure(it)
                null
            }
            if (summary != null && survey != null) {
                _uiState.value = InsightState.Success(summary, survey)
            }
        }
    }

    fun postSurvey() {
        viewModelScope.launch {
            when (val state = uiState.value) {
                is InsightState.Success -> {
                    if (state.survey.submitted.not()) insightRepository.postSurvey()
                    _uiState.value = state.copy(survey = state.survey.submit())
                }

                else -> return@launch
            }
        }
    }

    fun clearException() {
        _uiState.value = InsightState.Init
    }
}
