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

package team.capybara.moime.feature.insight.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moime.feature.insight.generated.resources.Res
import moime.feature.insight.generated.resources.survey_desc
import moime.feature.insight.generated.resources.survey_title
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.model.Survey
import team.capybara.moime.core.ui.component.MoimeSurveyButton
import team.capybara.moime.core.ui.compositionlocal.LocalScreenSize

@Composable
fun InsightFriendContent(
    survey: Survey,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val screenSize = LocalScreenSize.current
    val buttonSize = with(density) { (screenSize.width / 2).toDp() }

    Column(
        modifier = modifier.then(Modifier.fillMaxSize()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MoimeSurveyButton(
            onClick = { onSubmit() },
            survey = survey,
            modifier = Modifier.size(buttonSize)
        )
        Spacer(Modifier.height(28.dp))
        Text(
            text = stringResource(Res.string.survey_title),
            color = Gray50,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.survey_desc),
            color = Gray400,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
