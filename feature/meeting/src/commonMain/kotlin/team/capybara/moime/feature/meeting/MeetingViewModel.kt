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

package team.capybara.moime.feature.meeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import io.github.vinceglb.filekit.core.FileKit
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import team.capybara.moime.core.common.util.DateUtil.now
import team.capybara.moime.core.common.util.DateUtil.toApiFormat
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.ui.jsbridge.ImageDownloadHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.feature.meeting.navigation.MeetingRoute

internal class MeetingViewModel(
    private val meetingRoute: MeetingRoute,
    private val onNavigateToCamera: () -> Unit
) : ViewModel() {

    val webViewUrl = WEB_VIEW_BASE_URL + when (meetingRoute.status) {
        Meeting.Status.New -> NEW_MEETING_URL
        Meeting.Status.Created -> "$CREATED_MEETING_URL?$MEETING_ID_KEY=${meetingRoute.id}"
        Meeting.Status.Ongoing -> "$ONGOING_MEETING_URL?$MEETING_ID_KEY=${meetingRoute.id}"
        Meeting.Status.Finished -> "$FINISHED_MEETING_URL?$MEETING_ID_KEY=${meetingRoute.id}"
        Meeting.Status.Completed -> "$COMPLETED_MEETING_URL?$MEETING_ID_KEY=${meetingRoute.id}"
        else -> ERROR_URL
    }

    val imageDownloadHandler = ImageDownloadHandler(
        methodName = BRIDGE_DOWNLOAD_IMAGE_METHOD_NAME,
        onDownload = { image ->
            viewModelScope.launch {
                FileKit.saveFile(
                    baseName = "${meetingRoute.title}-${LocalDateTime.now().toApiFormat()}",
                    extension = DOWNLOAD_IMAGE_FORMAT,
                    bytes = image
                )
            }
        }
    )

    inner class CameraJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            MainScope().launch {
                onNavigateToCamera()
            }
        }

        override fun methodName(): String = BRIDGE_CAMERA_NAVIGATION_METHOD_NAME
    }

    companion object {
        private const val BRIDGE_CAMERA_NAVIGATION_METHOD_NAME = "onNavigateCamera"
        private const val BRIDGE_DOWNLOAD_IMAGE_METHOD_NAME = "onDownloadEndingImage"

        private const val CREATED_MEETING_URL = "upcoming-gathering"
        private const val ONGOING_MEETING_URL = "ongoing-gathering"
        private const val FINISHED_MEETING_URL = "ended-gathering"
        private const val COMPLETED_MEETING_URL = "memory-gathering"
        private const val NEW_MEETING_URL = "create-gathering"
        private const val ERROR_URL = "error"
        private const val MEETING_ID_KEY = "moimId"

        private const val DOWNLOAD_IMAGE_FORMAT = "jpg"
    }
}
