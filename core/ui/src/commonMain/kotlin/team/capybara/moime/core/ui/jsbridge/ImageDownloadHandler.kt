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
import com.multiplatform.webview.jsbridge.processParams
import com.multiplatform.webview.web.WebViewNavigator
import team.capybara.moime.core.common.util.Base64Util.decodeFromBase64

class ImageDownloadHandler(
    private val methodName: String = DEFAULT_BRIDGE_METHOD_NAME,
    private val onDownload: (ByteArray) -> Unit
) : IJsMessageHandler {

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        val imageJsMessage = processParams<ImageStringData>(message)
        onDownload(imageJsMessage.image.decodeFromBase64())
        callback(CALLBACK)
    }

    override fun methodName(): String = methodName

    companion object {
        private const val DEFAULT_BRIDGE_METHOD_NAME = "onDownloadImage"
        private const val CALLBACK = "SUCCESS"
    }
}
