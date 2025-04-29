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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import team.capybara.moime.core.designsystem.component.MoimeLoading
import team.capybara.moime.feature.insight.component.InsightFriendContent
import team.capybara.moime.feature.insight.component.InsightSummaryContent

@Composable
internal fun InsightScreen(
    currentSubTab: InsightSubTab,
    onNavigateToFriendDetail: (id: Long) -> Unit,
) {
    val insightViewModel = koinViewModel<InsightViewModel>()
    val uiState by insightViewModel.uiState.collectAsState()

    when (val state = uiState) {
        InsightState.Init -> {}

        InsightState.Loading -> {
            MoimeLoading()
        }

        is InsightState.Success -> {
            when (currentSubTab) {
                InsightSubTab.Summary -> {
                    InsightSummaryContent(
                        summary = state.summary,
                        onNavigateToFriendDetail = onNavigateToFriendDetail
                    )
                }

                InsightSubTab.Friend -> {
                    InsightFriendContent(
                        survey = state.survey,
                        onSubmit = insightViewModel::postSurvey,
                    )
                }
            }
        }

        is InsightState.Failure -> {

        }
    }
}
