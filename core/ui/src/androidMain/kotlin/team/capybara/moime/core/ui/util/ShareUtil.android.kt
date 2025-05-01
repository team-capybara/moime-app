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

package team.capybara.moime.core.ui.util

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

actual object ShareUtil {
    private var activityProvider: () -> Activity = {
        throw IllegalArgumentException(
            "You need to implement the 'activityProvider' to provide the required Activity. " +
                "Just make sure to set a valid activity using " +
                "the 'setActivityProvider()' method.",
        )
    }

    fun setActivityProvider(provider: () -> Activity) {
        activityProvider = provider
    }

    actual fun shareText(text: String) {
        val intent =
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
        val intentChooser = Intent.createChooser(intent, null)
        activityProvider().startActivity(intentChooser)
    }

    actual fun shareImage(image: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        val uri = getImageUriFromBitmap(bitmap)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        activityProvider().startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    private fun getImageUriFromBitmap(bitmap: android.graphics.Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(activityProvider().contentResolver, bitmap, "Shared Image", null)
        return Uri.parse(path)
    }
}
