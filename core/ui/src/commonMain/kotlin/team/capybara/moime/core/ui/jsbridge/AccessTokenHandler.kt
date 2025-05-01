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

package team.capybara.moime.core.ui.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.dataToJsonString
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import team.capybara.moime.core.common.model.BearerTokenStorage

class AccessTokenHandler : IJsMessageHandler, KoinComponent {

    private val bearerTokenStorage: BearerTokenStorage by inject()

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        bearerTokenStorage.lastOrNull()?.accessToken?.let { token ->
            callback(dataToJsonString(AccessTokenCallback(token)))
        }
    }

    override fun methodName(): String = METHOD_NAME

    companion object {
        private const val METHOD_NAME = "getAccessToken"
    }
}

@Serializable
data class AccessTokenCallback(
    val accessToken: String
)
