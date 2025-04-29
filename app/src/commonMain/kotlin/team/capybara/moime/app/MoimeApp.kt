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

package team.capybara.moime.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import dev.chrisbanes.haze.HazeState
import team.capybara.moime.core.designsystem.component.MoimeToast
import team.capybara.moime.core.designsystem.component.ToastState
import team.capybara.moime.core.designsystem.theme.MoimeTheme
import team.capybara.moime.core.ui.compositionlocal.LocalHazeState
import team.capybara.moime.core.ui.compositionlocal.LocalScreenSize
import team.capybara.moime.core.ui.compositionlocal.LocalToastHandler
import team.capybara.moime.core.ui.compositionlocal.ScreenSize
import team.capybara.moime.core.ui.compositionlocal.ToastHandler

@Composable
fun MoimeApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader
            .Builder(context)
            .crossfade(true)
            .build()
    }

    var screenSize by remember { mutableStateOf(ScreenSize()) }
    var toastState by remember { mutableStateOf<ToastState?>(null) }

    Layout(
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                MoimeTheme {
                    CompositionLocalProvider(
                        LocalScreenSize provides screenSize,
                        LocalHazeState provides HazeState(),
                        LocalToastHandler provides
                                ToastHandler(
                                    isShowing = toastState != null,
                                    onShow = { toastState = it },
                                ),
                    ) {
                        MoimeNavHost()
                    }
                }
                toastState?.let {
                    MoimeToast(
                        state = it,
                        onDismissRequest = { toastState = null },
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        },
        measurePolicy = { measurables, constraints ->
            val width = constraints.maxWidth
            val height = constraints.maxHeight

            screenSize = ScreenSize(width, height)

            val placeables =
                measurables.map { measurable ->
                    measurable.measure(constraints)
                }

            layout(width, height) {
                var yPosition = 0
                placeables.forEach { placeable ->
                    placeable.placeRelative(x = 0, y = yPosition)
                    yPosition += placeable.height
                }
            }
        },
    )
}
