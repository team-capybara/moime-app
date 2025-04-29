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

package team.capybara.moime.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import moime.feature.splash.generated.resources.Res
import moime.feature.splash.generated.resources.img_splash
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import team.capybara.moime.core.ui.component.SafeAreaColumn

@Composable
internal fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val screenModel = koinViewModel<SplashViewModel>()
    val state by screenModel.uiState.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            SplashState.Authorized -> onNavigateToMain()
            SplashState.Unauthorized -> onNavigateToLogin()
            else -> {}
        }
    }

    SafeAreaColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(Res.drawable.img_splash),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}
