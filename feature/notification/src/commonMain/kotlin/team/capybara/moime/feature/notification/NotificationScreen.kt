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

package team.capybara.moime.feature.notification

import androidx.compose.runtime.Composable
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.PopHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL

@Composable
internal fun NotificationScreen(
    onNavigateToBack: () -> Unit,
    onRefreshUnreadNotification: () -> Unit
) {
    val popHandler = PopHandler { onNavigateToBack() }

    MoimeWebView(
        url = WEB_VIEW_BASE_URL + WEB_VIEW_URL_PATH_NOTIFICATION,
        jsMessageHandlers = listOf(popHandler),
        onDispose = { onRefreshUnreadNotification() }
    )
}

private const val WEB_VIEW_URL_PATH_NOTIFICATION = "notification"
