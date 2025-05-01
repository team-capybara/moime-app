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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual fun ByteArray.resize(
    resizeOptions: ResizeOptions
): ByteArray {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }

    BitmapFactory.decodeByteArray(this, 0, size, options)

    var inSampleSize = 1
    while (options.outWidth / inSampleSize > resizeOptions.maxWidth ||
        options.outHeight / inSampleSize > resizeOptions.maxHeight
    ) {
        inSampleSize *= 2
    }

    options.inJustDecodeBounds = false
    options.inSampleSize = inSampleSize

    val resizedBitmap = BitmapFactory.decodeByteArray(this, 0, size, options)

    ByteArrayOutputStream().use { byteArrayOutputStream ->
        resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            byteArrayOutputStream
        )
        return byteArrayOutputStream.toByteArray()
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}
