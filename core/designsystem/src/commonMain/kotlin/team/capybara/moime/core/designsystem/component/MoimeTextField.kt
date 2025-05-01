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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moime.core.designsystem.generated.resources.cancel
import moime.core.designsystem.generated.resources.ic_search
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.theme.Gray400
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray500
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
fun MoimeTextField(
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onDone: ((String) -> Unit)? = null,
    onSearch: ((String) -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    hintTextRes: StringResource? = null,
    singleLine: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    fun submit(value: String, callback: (String) -> Unit) {
        if (text.isNotBlank()) {
            callback(value)
            submitted = true
        }
        keyboardController?.hide()
    }

    Row(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            enabled = enabled,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { onDone?.let { submit(text, it) } },
                onSearch = { onSearch?.let { submit(text, it) } }
            ),
            textStyle = TextStyle.Default.copy(
                color = Gray50,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            ),
            cursorBrush = SolidColor(Gray50),
            singleLine = singleLine,
            decorationBox = { innerTextField ->
                Surface(
                    color = Gray500,
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MOIME_TEXT_FIELD_HEIGHT)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty() && hintTextRes != null) {
                            Text(
                                text = stringResource(hintTextRes),
                                color = Gray50.copy(alpha = 0.3f)
                            )
                        } else {
                            innerTextField()
                        }
                        if (submitted.not() && onSearch != null) {
                            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                                FilledIconButton(
                                    onClick = { submit(text, onSearch) },
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    colors = IconButtonDefaults.filledIconButtonColors(
                                        contentColor = Gray400,
                                        containerColor = Gray500
                                    )
                                ) {
                                    Icon(
                                        painterResource(MoimeRes.drawable.ic_search),
                                        modifier = Modifier.size(24.dp),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
        AnimatedVisibility(
            visible = submitted
        ) {
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = {
                text = ""
                submitted = false
                onDismiss?.let { it() }
            }) {
                Text(
                    text = stringResource(MoimeRes.string.cancel),
                    color = Gray400,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private val MOIME_TEXT_FIELD_HEIGHT = 56.dp
