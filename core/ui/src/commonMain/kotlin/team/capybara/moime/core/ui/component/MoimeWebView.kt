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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import team.capybara.moime.core.designsystem.component.MoimeLoading
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.ui.jsbridge.AccessTokenHandler

@Composable
fun MoimeWebView(
    url: String,
    jsMessageHandlers: List<IJsMessageHandler> = emptyList(),
    onDispose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val webViewNavigator = rememberWebViewNavigator()
    val webViewState = rememberWebViewState(url)
    val jsBridge = rememberWebViewJsBridge(webViewNavigator)

    DisposableEffect(jsBridge) {
        with(jsBridge) {
            jsMessageHandlers.forEach { register(it) }
            register(AccessTokenHandler())
        }
        onDispose {
            onDispose?.let { it() }
        }
    }

    SafeAreaColumn {
        Box {
            WebView(
                state = webViewState.apply {
                    webSettings.backgroundColor = Gray700
                    webSettings.androidWebSettings.domStorageEnabled = true
                },
                navigator = webViewNavigator,
                webViewJsBridge = jsBridge,
                modifier = modifier.then(Modifier.fillMaxSize())
            )
            if (webViewState.isLoading) {
                MoimeLoading()
            }
        }
    }
}
