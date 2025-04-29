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

package team.capybara.moime.feature.friend.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moime.core.designsystem.generated.resources.cancel
import moime.core.designsystem.generated.resources.close
import moime.core.designsystem.generated.resources.confirm
import moime.feature.friend.generated.resources.Res
import moime.feature.friend.generated.resources.block
import moime.feature.friend.generated.resources.block_friend_dialog
import moime.feature.friend.generated.resources.block_friend_dialog_desc
import moime.feature.friend.generated.resources.create_meeting
import moime.feature.friend.generated.resources.failed_to_add_friend
import moime.feature.friend.generated.resources.failed_to_add_friend_desc
import moime.feature.friend.generated.resources.failed_to_block_friend
import moime.feature.friend.generated.resources.failed_to_block_friend_desc
import moime.feature.friend.generated.resources.failed_to_unblock_friend
import moime.feature.friend.generated.resources.failed_to_unblock_friend_desc
import moime.feature.friend.generated.resources.friend_added
import moime.feature.friend.generated.resources.friend_added_desc
import moime.feature.friend.generated.resources.unblock
import moime.feature.friend.generated.resources.unblock_friend_dialog
import moime.feature.friend.generated.resources.unblock_friend_dialog_desc
import org.jetbrains.compose.resources.getString
import team.capybara.moime.core.data.repository.FriendRepository
import team.capybara.moime.core.data.repository.MeetingRepository
import team.capybara.moime.core.designsystem.component.DialogRequest
import team.capybara.moime.core.model.Friend
import team.capybara.moime.feature.friend.FriendViewModel
import team.capybara.moime.feature.friend.navigation.FriendDetailRoute
import moime.core.designsystem.generated.resources.Res as MoimeRes

internal class FriendDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val friendViewModel: FriendViewModel,
    private val friendRepository: FriendRepository,
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val targetId = savedStateHandle.toRoute<FriendDetailRoute>().targetId

    private val _uiState = MutableStateFlow(FriendDetailState(Friend.init(targetId)))
    val uiState: StateFlow<FriendDetailState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        _uiState.value = FriendDetailState(Friend.init(targetId))
        getStranger()
        getMeetingsTotalCount()
        loadMeetings()
    }

    private fun getStranger() {
        viewModelScope.launch {
            friendRepository.getStranger(targetId)
                .onSuccess { stranger ->
                    _uiState.value = uiState.value.copy(stranger = stranger)
                }
                .onFailure {
                    _uiState.value = uiState.value.copy(exception = it)
                }
        }
    }

    private fun getMeetingsTotalCount() {
        viewModelScope.launch {
            meetingRepository.getMeetingsCountWith(targetId)
                .onSuccess { totalCount ->
                    _uiState.value = uiState.value.copy(meetingsTotalCount = totalCount)
                }
                .onFailure {
                    _uiState.value = uiState.value.copy(exception = it)
                }
        }
    }

    fun loadMeetings() {
        if (uiState.value.meetings.canRequest().not() || _uiState.value.exception != null) return
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(meetings = uiState.value.meetings.loading())
            meetingRepository.getMeetingsWith(targetId, uiState.value.meetings.nextRequest())
                .onSuccess { nextMeetings ->
                    _uiState.value = uiState.value.copy(
                        meetings = uiState.value.meetings.concatenate(nextMeetings)
                    )
                }.onFailure {
                    _uiState.value =
                        uiState.value.copy(
                            meetings = uiState.value.meetings.loading(false),
                            exception = it
                        )
                }
        }
    }

    fun addFriend(action: () -> Unit) {
        viewModelScope.launch {
            friendRepository.addFriend(targetId)
                .onSuccess {
                    refreshFriendScreen()
                    showDialog(
                        DialogRequest(
                            title = getString(
                                Res.string.friend_added,
                                uiState.value.stranger.nickname
                            ),
                            description = getString(
                                Res.string.friend_added_desc,
                                uiState.value.stranger.nickname
                            ),
                            actionTextRes = Res.string.create_meeting,
                            subActionTextRes = MoimeRes.string.close,
                            onAction = action,
                            onSubAction = ::hideDialog
                        )
                    )
                }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.failed_to_add_friend),
                            description = getString(Res.string.failed_to_add_friend_desc),
                            actionTextRes = MoimeRes.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    fun block() {
        val targetNickname = uiState.value.stranger.nickname
        viewModelScope.launch {
            showDialog(
                DialogRequest(
                    title = getString(Res.string.block_friend_dialog, targetNickname),
                    description = getString(Res.string.block_friend_dialog_desc, targetNickname),
                    actionTextRes = Res.string.block,
                    subActionTextRes = MoimeRes.string.cancel,
                    onAction = {
                        hideDialog()
                        onBlock()
                    },
                    onSubAction = ::hideDialog
                )
            )
        }
    }

    private fun onBlock() {
        viewModelScope.launch {
            friendRepository.blockFriend(targetId)
                .onSuccess {
                    refreshFriendScreen()
                    _uiState.value = uiState.value.copy(
                        stranger = uiState.value.stranger.copy(blocked = true)
                    )
                }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.failed_to_block_friend),
                            description = getString(Res.string.failed_to_block_friend_desc),
                            actionTextRes = MoimeRes.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    fun unblock() {
        val targetNickname = uiState.value.stranger.nickname
        viewModelScope.launch {
            showDialog(
                DialogRequest(
                    title = getString(Res.string.unblock_friend_dialog, targetNickname),
                    description = getString(Res.string.unblock_friend_dialog_desc, targetNickname),
                    actionTextRes = Res.string.unblock,
                    subActionTextRes = MoimeRes.string.cancel,
                    onAction = {
                        hideDialog()
                        onUnblock()
                    },
                    onSubAction = ::hideDialog
                )
            )
        }
    }

    private fun onUnblock() {
        viewModelScope.launch {
            friendRepository.unblockFriend(targetId)
                .onSuccess {
                    refreshFriendScreen()
                    _uiState.value = uiState.value.copy(
                        stranger = uiState.value.stranger.copy(blocked = false)
                    )
                }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.failed_to_unblock_friend),
                            description = getString(Res.string.failed_to_unblock_friend_desc),
                            actionTextRes = MoimeRes.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    private fun showDialog(request: DialogRequest) {
        _uiState.value = uiState.value.copy(dialogRequest = request)
    }

    private fun refreshFriendScreen() {
        friendViewModel.refresh()
    }

    fun hideDialog() {
        _uiState.value = uiState.value.copy(dialogRequest = null)
    }

    fun clearException() {
        _uiState.value = uiState.value.copy(exception = null)
    }
}
