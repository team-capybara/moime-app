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

package team.capybara.moime.feature.mypage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import coil3.compose.LocalPlatformContext
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.capybara.moime.core.common.util.Base64Util.encodeToBase64
import team.capybara.moime.core.ui.component.MoimeImagePicker
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.ImageStringData
import team.capybara.moime.core.ui.jsbridge.PopHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.core.ui.util.CoilUtil

@Composable
internal fun MyPageScreen(
    onLogout: () -> Unit,
    onRefreshProfileImage: () -> Unit,
    onNavigateToBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController =
        remember(permissionFactory) {
            permissionFactory.createPermissionsController()
        }
    BindEffect(permissionController)

    val viewModel = remember { MyPageViewModel(permissionController, onLogout) }
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalPlatformContext.current
    val popHandler = PopHandler { onNavigateToBack() }

    LaunchedEffect(uiState) {
        snapshotFlow { uiState.logoutRequested }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onNavigateToLogin()
                onRefreshProfileImage()
            }
    }

    MoimeWebView(
        url = WEB_VIEW_BASE_URL + MyPageViewModel.WEB_VIEW_URL_PATH_MY_PAGE,
        jsMessageHandlers =
        viewModel.jsMessageHandler.getHandlers() +
                listOf(
                    viewModel.imagePickerHandler,
                    popHandler,
                ),
        onDispose = {
            CoilUtil.clearDiskCache(context)
            CoilUtil.clearMemoryCache(context)
        }
    )

    uiState.onImagePicked?.let { callback ->
        MoimeImagePicker(onPicked = {
            callback(Json.encodeToString(ImageStringData(it.encodeToBase64())))
        })
    }
}
