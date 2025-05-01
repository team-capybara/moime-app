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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import team.capybara.moime.core.ui.util.ResizeOptions
import team.capybara.moime.core.ui.util.resize

@Composable
fun MoimeImagePicker(
    onPicked: (ByteArray) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
    resizeOptions: ResizeOptions = ResizeOptions(512, 512, 512L)
) {
    val imagePicker = rememberFilePickerLauncher(
        type = PickerType.Image,
        onResult = { result ->
            scope.launch {
                val byteArray = result?.readBytes()?.resize(resizeOptions) ?: return@launch
                onPicked(byteArray)
            }
        }
    )
    scope.launch { imagePicker.launch() }
}
