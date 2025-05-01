import SwiftUI
import app
import FirebaseCore
import FirebaseMessaging

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        NotifierManager.shared.initialize(
            configuration: NotificationPlatformConfigurationIos(
                showPushNotification: true,
                askNotificationPermissionOnStart: true
            )
        )

        return true
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }

     func application(
         _ application: UIApplication,
         didReceiveRemoteNotification userInfo: [AnyHashable : Any]
     ) async -> UIBackgroundFetchResult {
          NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
          return UIBackgroundFetchResult.newData
     }
}

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}

	init() {
	    KoinKt.doInitKoin()
	}
}
