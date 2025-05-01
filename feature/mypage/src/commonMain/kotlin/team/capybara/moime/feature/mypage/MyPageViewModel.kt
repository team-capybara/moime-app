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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multiplatform.webview.cookie.WebViewCookieManager
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import team.capybara.moime.core.data.repository.api.UserRepository
import team.capybara.moime.core.ui.jsbridge.APP_VERSION
import team.capybara.moime.core.ui.jsbridge.ImagePickerHandler
import team.capybara.moime.feature.mypage.jsbridge.AppVersionJsCallback
import team.capybara.moime.feature.mypage.jsbridge.MyPageJsMessageHandler
import team.capybara.moime.feature.mypage.jsbridge.PermissionJsCallback

internal class MyPageViewModel(
    private val permissionsController: PermissionsController,
    private val onLogout: () -> Unit
) : ViewModel(), KoinComponent {

    private val userRepository: UserRepository by inject()

    private val _uiState = MutableStateFlow(MyPageState())
    val uiState: StateFlow<MyPageState> = _uiState.asStateFlow()

    val jsMessageHandler = MyPageJsMessageHandler(
        onGetNotificationPermission = ::onGetNotificationPermission,
        onGetAppVersion = ::onGetAppVersion,
        onLogout = ::logout
    )

    val imagePickerHandler = ImagePickerHandler { callback ->
        _uiState.value = uiState.value.copy(onImagePicked = callback)
    }

    private fun onGetNotificationPermission(callback: (String) -> Unit) {
        viewModelScope.launch {
            val jsCallbackResponse = PermissionJsCallback(
                granted = permissionsController.isPermissionGranted(Permission.REMOTE_NOTIFICATION)
            )
            callback(Json.encodeToString((jsCallbackResponse)))
        }
    }

    private fun onGetAppVersion(callback: (String) -> Unit) {
        val jsCallbackResponse = AppVersionJsCallback(
            version = APP_VERSION
        )
        callback(Json.encodeToString((jsCallbackResponse)))
    }

    private fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            WebViewCookieManager().removeAllCookies()
            _uiState.value = uiState.value.copy(logoutRequested = true)
            onLogout()
        }
    }

    companion object {
        internal const val WEB_VIEW_URL_PATH_MY_PAGE = "mypage"
    }
}
