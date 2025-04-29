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

package team.capybara.moime.feature.camera

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch
import moime.core.designsystem.generated.resources.ic_close
import moime.core.designsystem.generated.resources.ic_delete
import moime.core.designsystem.generated.resources.ic_done
import moime.core.designsystem.generated.resources.ic_moment
import moime.core.designsystem.generated.resources.ic_refresh
import moime.core.designsystem.generated.resources.ic_reload
import moime.core.designsystem.generated.resources.ic_upload
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.core.designsystem.theme.Gray300
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray500
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.designsystem.theme.Gray800
import team.capybara.moime.core.designsystem.theme.MoimeGreen
import team.capybara.moime.core.ui.component.SafeAreaColumn
import team.capybara.moime.core.ui.util.CameraMode
import team.capybara.moime.core.ui.util.PeekabooCamera
import team.capybara.moime.core.ui.util.rememberPeekabooCameraState
import team.capybara.moime.feature.meeting.camera.component.CameraDeniedCard
import team.capybara.moime.feature.meeting.camera.component.CameraToast
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
internal fun CameraScreen(
    meetingId: Long,
    onNavigateToBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }
    val locationTrackerFactory = rememberLocationTrackerFactory(LocationTrackerAccuracy.Best)
    val locationTracker = remember(locationTrackerFactory) {
        locationTrackerFactory.createLocationTracker(permissionController)
    }
    val viewModel = remember { CameraViewModel(meetingId, locationTracker) }

    val uiState by viewModel.uiState.collectAsState()
    val cameraState = rememberPeekabooCameraState(
        onCapture = viewModel::onCaptured
    )

    BindEffect(permissionController)

    BindLocationTrackerEffect(locationTracker)

    DisposableEffect(Unit) {
        scope.launch {
            val permissions = listOf(Permission.CAMERA, Permission.LOCATION)
            permissions.forEach {
                if (permissionController.isPermissionGranted(it).not()) {
                    permissionController.providePermission(it)
                }
            }
            viewModel.startLocationTracker()
        }
        onDispose {
            viewModel.stopLocationTracker()
            viewModel.clear()
        }
    }

    SafeAreaColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    onNavigateToBack()
                    viewModel.stopLocationTracker()
                    viewModel.clear()
                }
            ) {
                Icon(
                    painterResource(MoimeRes.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(Modifier.weight(76f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            PeekabooCamera(
                state = cameraState,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                permissionDeniedContent = { CameraDeniedCard() }
            )
            androidx.compose.animation.AnimatedVisibility(
                visible = cameraState.isCapturing,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Gray800.copy(alpha = 0.75f))
                        .clip(RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = MoimeGreen,
                        strokeWidth = 2.dp
                    )
                }
            }
            uiState.photo?.let { photo ->
                Image(
                    photo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                        .graphicsLayer {
                            this.rotationY =
                                if (cameraState.cameraMode == CameraMode.Front) 180f else 0f
                        }
                )
            }
            if (uiState.photo != null &&
                (uiState.uploadState == CameraUploadState.Init ||
                        uiState.uploadState == CameraUploadState.Failure)
            ) {
                FilledIconButton(
                    onClick = { viewModel.clear() },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(36.dp)
                        .align(Alignment.BottomCenter),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Gray500,
                        contentColor = Gray50
                    )
                ) {
                    Icon(
                        painterResource(MoimeRes.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(Modifier.weight(48f))
        Row(
            modifier = Modifier.fillMaxWidth().weight(80f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (uiState.photo == null) {
                IconButton(
                    onClick = {},
                    enabled = with(cameraState) { isCameraReady && isCapturing.not() }
                ) {
                    Icon(
                        painterResource(MoimeRes.drawable.ic_moment),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .scale(56 / 80f),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            if (uiState.photo != null) {
                FilledIconButton(
                    onClick = {
                        if (uiState.uploadState != CameraUploadState.Uploading) {
                            viewModel.uploadPhoto()
                        }
                    },
                    modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MoimeGreen,
                        contentColor = Gray700,
                        disabledContainerColor = Gray800,
                        disabledContentColor = Gray50
                    ),
                    enabled = uiState.uploadState != CameraUploadState.Success
                ) {
                    if (uiState.uploadState == CameraUploadState.Uploading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                                .scale(28 / 80f),
                            color = Gray700,
                            strokeWidth = 2.dp
                        )
                    } else {
                        when (uiState.uploadState) {
                            CameraUploadState.Init -> MoimeRes.drawable.ic_upload
                            CameraUploadState.Success -> MoimeRes.drawable.ic_done
                            CameraUploadState.Failure -> MoimeRes.drawable.ic_reload
                            CameraUploadState.Uploading -> null
                        }?.let {
                            Icon(
                                painterResource(it),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .scale(36 / 80f)
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(width = 4.dp, color = MoimeGreen, shape = CircleShape)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    FilledTonalButton(
                        onClick = { cameraState.capture() },
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .scale(64 / 80f),
                        shape = CircleShape,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Gray50,
                            disabledContainerColor = Gray300
                        ),
                        enabled = with(cameraState) {
                            isCameraReady && isCapturing.not() && uiState.location != null
                        }
                    ) {}
                }
            }
            if (uiState.photo == null) {
                IconButton(
                    onClick = { cameraState.toggleCamera() },
                    enabled = with(cameraState) { isCameraReady && isCapturing.not() }
                ) {
                    Icon(
                        painterResource(MoimeRes.drawable.ic_refresh),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .scale(56 / 80f),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        Spacer(Modifier.weight(40f))
        uiState.toast?.let { CameraToast(it) } ?: Spacer(Modifier.weight(36f))
        Spacer(Modifier.weight(80f))
    }
}