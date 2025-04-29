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

package team.capybara.moime.feature.mypage.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class MyPageJsMessageHandler(
    private val onGetNotificationPermission: ((String) -> Unit) -> Unit,
    private val onGetAppVersion: ((String) -> Unit) -> Unit,
    private val onLogout: () -> Unit
) {

    inner class PermissionJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            onGetNotificationPermission(callback)
        }

        override fun methodName(): String = "onGetNotificationPermission"
    }

    inner class AppVersionJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            onGetAppVersion(callback)
        }

        override fun methodName(): String = "onGetAppVersion"
    }

    inner class LogoutJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            onLogout()
        }

        override fun methodName(): String = "onLogout"
    }

    fun getHandlers() = listOf(
        PermissionJsMessageHandler(),
        AppVersionJsMessageHandler(),
        LogoutJsMessageHandler()
    )
}
