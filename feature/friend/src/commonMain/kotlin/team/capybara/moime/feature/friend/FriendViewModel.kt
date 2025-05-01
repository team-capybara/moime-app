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

package team.capybara.moime.feature.friend

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moime.core.designsystem.generated.resources.close
import moime.core.designsystem.generated.resources.confirm
import moime.feature.friend.generated.resources.Res
import moime.feature.friend.generated.resources.cannot_find_friend
import moime.feature.friend.generated.resources.cannot_find_friend_desc
import moime.feature.friend.generated.resources.create_meeting
import moime.feature.friend.generated.resources.failed_to_add_friend
import moime.feature.friend.generated.resources.failed_to_add_friend_desc
import moime.feature.friend.generated.resources.failed_to_unblock_friend
import moime.feature.friend.generated.resources.failed_to_unblock_friend_desc
import moime.feature.friend.generated.resources.friend_added
import moime.feature.friend.generated.resources.friend_added_desc
import org.jetbrains.compose.resources.getString
import team.capybara.moime.core.common.model.CursorData
import team.capybara.moime.core.data.repository.api.FriendRepository
import team.capybara.moime.core.designsystem.component.DialogRequest
import team.capybara.moime.core.model.Friend
import team.capybara.moime.feature.friend.navigation.FriendRoute
import moime.core.designsystem.generated.resources.Res as MoimeRes

internal class FriendViewModel(
    savedStateHandle: SavedStateHandle,
    private val friendRepository: FriendRepository
) : ViewModel() {

    private val code = savedStateHandle.toRoute<FriendRoute>().userCode
    private val profileImageUrl = savedStateHandle.toRoute<FriendRoute>().userProfileImageUrl

    private val _uiState = MutableStateFlow(FriendState(code, profileImageUrl))
    val uiState: StateFlow<FriendState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = FriendState(code, profileImageUrl)
        getFriendsCount()
        getBlockedFriendsCount()
        loadMyFriends()
        loadRecommendedFriends()
        loadBlockedFriends()
    }

    private fun getFriendsCount() {
        viewModelScope.launch {
            friendRepository.getMyFriendsCount()
                .onSuccess { _uiState.value = uiState.value.copy(friendsCount = it) }
                .onFailure { _uiState.value = uiState.value.copy(exception = it) }
        }
    }

    fun loadMyFriends() {
        if (uiState.value.myFriends.canRequest().not()) return
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(myFriends = uiState.value.myFriends.loading())
            friendRepository.getMyFriends(
                cursor = uiState.value.myFriends.nextRequest()
            ).onSuccess { nextFriends ->
                _uiState.value = uiState.value.copy(
                    myFriends = uiState.value.myFriends.concatenate(nextFriends)
                )
            }.onFailure {
                _uiState.value = uiState.value.copy(exception = it)
            }
        }
    }

    fun loadRecommendedFriends() {
        if (uiState.value.recommendedFriends.canRequest().not()) return
        viewModelScope.launch {
            _uiState.value =
                uiState.value.copy(recommendedFriends = uiState.value.recommendedFriends.loading())
            friendRepository.getRecommendedFriends(
                cursor = uiState.value.recommendedFriends.nextRequest()
            ).onSuccess { nextFriends ->
                _uiState.value = uiState.value.copy(
                    recommendedFriends = uiState.value.recommendedFriends.concatenate(nextFriends)
                )
            }.onFailure {
                _uiState.value = uiState.value.copy(exception = it)
            }
        }
    }

    fun searchMyFriends(nickname: String) {
        if (nickname.isBlank()) {
            clearSearchedMyFriends()
            return
        }
        if (uiState.value.searchedMyFriends?.canRequest() == false) return
        viewModelScope.launch {
            _uiState.value =
                uiState.value.copy(searchedMyFriends = uiState.value.searchedMyFriends?.loading())
            if (uiState.value.searchedMyFriends == null) {
                _uiState.value = uiState.value.copy(searchedMyFriends = CursorData())
            }
            friendRepository.getMyFriends(
                cursor = uiState.value.searchedMyFriends?.nextRequest() ?: return@launch,
                nickname = nickname
            ).onSuccess { nextFriends ->
                _uiState.value = uiState.value.copy(
                    searchedMyFriends = uiState.value.searchedMyFriends?.concatenate(nextFriends)
                )
            }.onFailure {
                _uiState.value = uiState.value.copy(exception = it)
            }
        }
    }

    fun searchRecommendedFriends(nickname: String) {
        if (nickname.isBlank()) {
            clearSearchedRecommendedFriends()
            return
        }
        if (uiState.value.searchedRecommendedFriends?.canRequest() == false) return
        viewModelScope.launch {
            _uiState.value =
                uiState.value.copy(searchedRecommendedFriends = uiState.value.searchedRecommendedFriends?.loading())
            if (uiState.value.searchedRecommendedFriends == null) {
                _uiState.value = uiState.value.copy(searchedRecommendedFriends = CursorData())
            }
            friendRepository.getRecommendedFriends(
                cursor = uiState.value.searchedRecommendedFriends?.nextRequest() ?: return@launch,
                nickname = nickname
            ).onSuccess { nextFriends ->
                _uiState.value = uiState.value.copy(
                    searchedRecommendedFriends = uiState.value.searchedRecommendedFriends
                        ?.concatenate(nextFriends)
                )
            }.onFailure {
                _uiState.value = uiState.value.copy(exception = it)
            }
        }
    }

    fun findUser(code: String) {
        viewModelScope.launch {
            friendRepository.getStranger(code)
                .onSuccess { _uiState.value = uiState.value.copy(foundUser = it) }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.cannot_find_friend),
                            description = getString(Res.string.cannot_find_friend_desc),
                            actionTextRes = MoimeRes.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    fun clearSearchedMyFriends() {
        _uiState.value = uiState.value.copy(searchedMyFriends = null)
    }

    fun clearSearchedRecommendedFriends() {
        _uiState.value = uiState.value.copy(searchedRecommendedFriends = null)
    }

    fun clearFoundUser() {
        _uiState.value = uiState.value.copy(foundUser = null)
    }

    fun addFriend(target: Friend, action: () -> Unit) {
        viewModelScope.launch {
            friendRepository.addFriend(target.id)
                .onSuccess {
                    clearFoundUser()
                    _uiState.value = uiState.value.copy(
                        friendsCount = uiState.value.friendsCount + 1,
                        myFriends = uiState.value.myFriends.copy(
                            data = listOf(target) + uiState.value.myFriends.data
                        ),
                        recommendedFriends = uiState.value.recommendedFriends.copy(
                            data = uiState.value.recommendedFriends.data.filter { it.id != target.id }
                        )
                    )

                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.friend_added, target.nickname),
                            description = getString(Res.string.friend_added_desc, target.nickname),
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

    private fun getBlockedFriendsCount() {
        viewModelScope.launch {
            friendRepository.getBlockedFriendsCount()
                .onSuccess { _uiState.value = uiState.value.copy(blockedFriendsCount = it) }
                .onFailure { _uiState.value = uiState.value.copy(exception = it) }
        }
    }

    fun loadBlockedFriends() {
        if (uiState.value.blockedFriends.canRequest().not()) return
        viewModelScope.launch {
            _uiState.value =
                uiState.value.copy(blockedFriends = uiState.value.blockedFriends.loading())
            friendRepository.getBlockedFriends(
                cursor = uiState.value.blockedFriends.nextRequest()
            ).onSuccess { nextFriends ->
                _uiState.value = uiState.value.copy(
                    blockedFriends = uiState.value.blockedFriends.concatenate(nextFriends)
                )
            }.onFailure {
                _uiState.value = uiState.value.copy(exception = it)
            }
        }
    }

    fun unblockFriend(targetId: Long) {
        viewModelScope.launch {
            friendRepository.unblockFriend(targetId)
                .onSuccess {
                    _uiState.value = uiState.value.copy(
                        blockedFriends = uiState.value.blockedFriends.copy(
                            data = uiState.value.blockedFriends.data.filter { it.id != targetId }
                        ),
                        blockedFriendsCount = uiState.value.blockedFriendsCount - 1
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

    fun hideDialog() {
        _uiState.value = uiState.value.copy(dialogRequest = null)
    }

    fun clearException() {
        _uiState.value = uiState.value.copy(exception = null)
    }
}
