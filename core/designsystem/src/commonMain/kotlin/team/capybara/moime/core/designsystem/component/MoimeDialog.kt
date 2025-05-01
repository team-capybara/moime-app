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

package team.capybara.moime.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.theme.Gray200
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray700

@Composable
fun MoimeDialog(
    request: DialogRequest,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier.then(Modifier.width(DIALOG_WIDTH).wrapContentHeight()),
            shape = RoundedCornerShape(12.dp),
            color = Gray50
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = request.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Gray700
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = request.description,
                    fontWeight = FontWeight.Normal,
                    color = Gray400,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    request.subActionTextRes?.let {
                        Button(
                            onClick = { request.onSubAction?.let { it() } },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Gray700,
                                containerColor = Gray200
                            ),
                            shape = RoundedCornerShape(30.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(it),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                maxLines = 1
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                    }
                    Button(
                        onClick = request.onAction,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Gray50,
                            containerColor = Gray700
                        ),
                        shape = RoundedCornerShape(30.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(request.actionTextRes),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

data class DialogRequest(
    val title: String,
    val description: String,
    val actionTextRes: StringResource,
    val subActionTextRes: StringResource? = null,
    val onAction: () -> Unit,
    val onSubAction: (() -> Unit)? = null,
)

private val DIALOG_WIDTH = 314.dp
