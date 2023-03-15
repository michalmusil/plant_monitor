package cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NotificationTokenManagerImpl: INotificationTokenManager {
    val TAG = "NotificationTokenManagerImpl"

    override suspend fun getNotificationToken(): String? {
        val token = suspendCoroutine<String?>{ continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener{ task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    continuation.resume(null)
                }
                else {
                    val token = task.result
                    continuation.resume(token)
                }
            }
        }
        return token
    }
}