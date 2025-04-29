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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.capybara.moime.feature.camera.navigation.CameraRoute
import team.capybara.moime.feature.camera.navigation.cameraScreen
import team.capybara.moime.feature.friend.navigation.FriendBlockListRoute
import team.capybara.moime.feature.friend.navigation.FriendDetailRoute
import team.capybara.moime.feature.friend.navigation.FriendRoute
import team.capybara.moime.feature.friend.navigation.friendScreen
import team.capybara.moime.feature.login.navigation.LoginRoute
import team.capybara.moime.feature.login.navigation.loginScreen
import team.capybara.moime.feature.main.navigation.MainRoute
import team.capybara.moime.feature.main.navigation.mainScreen
import team.capybara.moime.feature.meeting.navigation.MeetingRoute
import team.capybara.moime.feature.meeting.navigation.meetingScreen
import team.capybara.moime.feature.mypage.navigation.MyPageRoute
import team.capybara.moime.feature.mypage.navigation.myPageScreen
import team.capybara.moime.feature.notification.navigation.NotificationRoute
import team.capybara.moime.feature.notification.navigation.notificationScreen
import team.capybara.moime.feature.onboarding.navigation.OnboardingRoute
import team.capybara.moime.feature.onboarding.navigation.onboardingScreen
import team.capybara.moime.feature.splash.navigation.SplashRoute
import team.capybara.moime.feature.splash.navigation.splashScreen

@Composable
fun MoimeNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier
    ) {
        splashScreen(
            onNavigateToMain = {
                navController.navigate(MainRoute) {
                    popUpTo(SplashRoute) {
                        inclusive = true
                    }
                }
            },
            onNavigateToLogin = {
                navController.navigate(LoginRoute) {
                    popUpTo(SplashRoute) {
                        inclusive = true
                    }
                }
            }
        )

        onboardingScreen(
            onNavigateToMain = {
                navController.navigate(MainRoute) {
                    popUpTo(LoginRoute) {
                        inclusive = true
                    }
                }
            }
        )

        loginScreen(
            onNavigateToMain = {
                navController.navigate(MainRoute) {
                    popUpTo(LoginRoute) {
                        inclusive = true
                    }
                }
            },
            onNavigateToOnboarding = {
                navController.navigate(OnboardingRoute) {
                    popUpTo(LoginRoute) {
                        inclusive = true
                    }
                }
            }
        )

        mainScreen(
            onNavigateToMyPage = {
                navController.navigate(MyPageRoute)
            },
            onNavigateToFriendDetail = {
                navController.navigate(FriendDetailRoute(targetId = it))
            },
            onNavigateToFriend = {
                navController.navigate(
                    FriendRoute(
                        userCode = it.code,
                        userProfileImageUrl = it.profileImageUrl
                    )
                )
            },
            onNavigateToMeetingCreate = {
                navController.navigate(MeetingRoute.NEW)
            },
            onNavigateToNotification = {
                navController.navigate(NotificationRoute)
            },
            onNavigateToMeetingDetail = {
                navController.navigate(
                    MeetingRoute(
                        id = it.id,
                        title = it.title,
                        status = it.status
                    )
                )
            },
        )

        friendScreen(
            onNavigateToBack = {
                navController.navigateUp()
            },
            onNavigateToFriendDetail = {
                navController.navigate(FriendDetailRoute(targetId = it))
            },
            onNavigateToFriendBlockList = {
                navController.navigate(FriendBlockListRoute)
            },
            onNavigateToMeetingCreate = {
                navController.navigate(MeetingRoute.NEW)
            },
            onNavigateToMeetingDetail = {
                navController.navigate(
                    MeetingRoute(
                        id = it.id,
                        title = it.title,
                        status = it.status
                    )
                )
            }
        )

        myPageScreen(
            onLogout = {
                //TODO: LoginViewModel.reset()
            },
            onProfileImageChanged = {
                //TODO: MainViewModel.refresh()
            },
            onNavigateToBack = {
                navController.navigateUp()
            },
            onNavigateToLogin = {
                navController.navigate(LoginRoute) {
                    popUpTo(SplashRoute) {
                        inclusive = true
                    }
                }
            }
        )

        notificationScreen(
            onNavigateToBack = {
                navController.navigateUp()
            },
            onRefreshUnreadNotification = {
                //TODO: MainViewModel.refresh()
            }
        )

        meetingScreen(
            onRefreshMeetingList = {
                //TODO: HomeViewModel.refresh()
            },
            onNavigateToFriendDetail = {
                navController.navigate(FriendDetailRoute(targetId = it))
            },
            onNavigateToCamera = {
                navController.navigate(CameraRoute(meetingId = it))
            },
            onNavigateToBack = {
                navController.navigateUp()
            }
        )

        cameraScreen(
            onNavigateToBack = {
                navController.navigateUp()
            }
        )
    }
}
