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

package team.capybara.moime.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.permission.permissionUtil
import io.github.vinceglb.filekit.core.FileKit
import team.capybara.moime.core.ui.util.ShareUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarStyle()
        initNotifier()
        ShareUtil.setActivityProvider { return@setActivityProvider this }
        FileKit.init(this)
        setContent {
            MoimeApp()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        NotifierManager.onCreateOrOnNewIntent(intent)
    }

    private fun setSystemBarStyle() {
        enableEdgeToEdge(
            statusBarStyle =
            SystemBarStyle.dark(
                Color.TRANSPARENT,
            ),
            navigationBarStyle =
            SystemBarStyle.dark(
                Color.TRANSPARENT,
            ),
        )
    }

    private fun initNotifier() {
        val notificationPermissionUtil by permissionUtil()
        notificationPermissionUtil.askNotificationPermission()

        NotifierManager.onCreateOrOnNewIntent(intent)
    }
}
