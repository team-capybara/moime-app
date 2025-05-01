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

package team.capybara.moime.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.kmpnotifier.notification.NotifierManager
import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.cookie.WebViewCookieManager
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.dataToJsonString
import com.multiplatform.webview.jsbridge.processParams
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import team.capybara.moime.core.common.di.ScopeProvider
import team.capybara.moime.core.data.repository.api.UserRepository
import team.capybara.moime.core.ui.jsbridge.ImagePickerHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_COOKIE_DOMAIN
import team.capybara.moime.feature.login.bridge.LoginJsCallback
import team.capybara.moime.feature.login.bridge.LoginJsMessage

internal class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.InProgress())
    val uiState: StateFlow<LoginState> = _uiState

    val imagePickerHandler = ImagePickerHandler { callback ->
        if (uiState.value is LoginState.InProgress) {
            _uiState.value = LoginState.InProgress(onImagePicked = callback)
        }
    }

    inner class LoginJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            val loginJsMessage = processParams<LoginJsMessage>(message)
            loginJsMessage.accessToken.takeIf { it.isNotBlank() }?.let { accessToken ->
                viewModelScope.launch {
                    userRepository.login(accessToken)
                        .onSuccess {
                            ScopeProvider.closeScope()
                            ScopeProvider.createScope()

                            setWebViewCookieManager(accessToken)

                            NotifierManager.getPushNotifier().getToken()?.let {
                                callback(dataToJsonString(LoginJsCallback(it)))
                            }

                            _uiState.value =
                                LoginState.Success(isNewbie = loginJsMessage.isNewbie)
                        }
                        .onFailure { LoginState.Failure(it) }
                }
            } ?: run {
                _uiState.value = LoginState.Failure(Throwable("AccessToken is blank."))
            }
        }

        override fun methodName(): String = BRIDGE_LOGIN_METHOD_NAME
    }

    private suspend fun setWebViewCookieManager(token: String) {
        WebViewCookieManager().setCookie(
            url = WEB_VIEW_BASE_URL,
            cookie = Cookie(
                name = "Authorization",
                value = "Bearer $token",
                domain = WEB_VIEW_COOKIE_DOMAIN
            )
        )
    }

    fun reset() {
        _uiState.value = LoginState.InProgress()
    }

    companion object {
        const val WEB_VIEW_LOGIN_URL = "https://www.moime.app/login"
        private const val BRIDGE_LOGIN_METHOD_NAME = "onLoginSuccess"
    }
}
