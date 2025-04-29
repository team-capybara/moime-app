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

package team.capybara.moime.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray600
import team.capybara.moime.core.designsystem.theme.Gray800
import team.capybara.moime.core.ui.model.SubTab

@Composable
fun <T : SubTab> SubTabSegmentedButtonBar(
    subTabs: List<T>,
    selected: T,
    onSubTabChanged: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.then(Modifier.height(SEGMENTED_BUTTON_BAR_HEIGHT)),
        shape = RoundedCornerShape(100.dp),
        color = Gray800
    ) {
        Row(
            modifier = Modifier.fillMaxHeight().padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            subTabs.forEach {
                SubTabSegmentedButton(
                    enabled = selected.titleTextRes != it.titleTextRes,
                    text = it.getTitleText(),
                    onClick = { onSubTabChanged(it) }
                )
            }
        }
    }
}

@Composable
private fun SubTabSegmentedButton(
    enabled: Boolean,
    text: String,
    onClick: (() -> Unit)?
) {
    Button(
        onClick = onClick ?: {},
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Gray600,
            disabledContentColor = Gray50,
            containerColor = Gray800,
            contentColor = Gray400
        ),
        shape = RoundedCornerShape(100.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

private val SEGMENTED_BUTTON_BAR_HEIGHT = 44.dp