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

package team.capybara.moime.feature.meeting.camera.component

import moime.feature.camera.generated.resources.Res
import moime.feature.camera.generated.resources.failed_to_capture
import moime.feature.camera.generated.resources.failed_to_get_location
import moime.feature.camera.generated.resources.loading_location
import moime.feature.camera.generated.resources.upload_done
import moime.feature.camera.generated.resources.upload_failure
import moime.feature.camera.generated.resources.uploading_photo
import org.jetbrains.compose.resources.StringResource

internal enum class CameraToastType(
    val message: StringResource,
    val type: Type
) {
    LoadingLocation(
        message = Res.string.loading_location,
        type = Type.Normal
    ),
    LocationFailure(
        message = Res.string.failed_to_get_location,
        type = Type.Error
    ),
    CaptureFailure(
        message = Res.string.failed_to_capture,
        type = Type.Error
    ),
    UploadSuccess(
        message = Res.string.upload_done,
        type = Type.Normal
    ),
    UploadFailure(
        message = Res.string.upload_failure,
        type = Type.Error
    ),
    Uploading(
        message = Res.string.uploading_photo,
        type = Type.Normal
    );

    enum class Type {
        Normal, Error
    }
}
