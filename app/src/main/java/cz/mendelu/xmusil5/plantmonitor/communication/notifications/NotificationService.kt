package cz.mendelu.xmusil5.plantmonitor.communication.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.activities.MainActivity
import kotlin.random.Random

class NotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val titleString = this.getString(R.string.plantNotFeelingWellTitle)
        val messageEnding = this.getString(R.string.plantNotFeelingWellMessageEnd)
        val messagePlantName = message.data[NotificationConstants.NOTIFICATION_PLANT_NAME_KEY]
        if(messagePlantName.isNullOrBlank()) {
            return
        }
        val messageString = "${messagePlantName} ${messageEnding}"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt(from = 1, until = Int.MAX_VALUE)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel(notificationManager)
        }

        val intent = Intent(this, MainActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)

        val notification = NotificationCompat
            .Builder(this, NotificationConstants.CHANNEL_ID)
            .setContentTitle(titleString)
            .setContentText(messageString)
            .setSmallIcon(R.drawable.app_logo_monochrome)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NotificationConstants.CHANNEL_ID,
            NotificationConstants.CHANNEL_NAME,
            IMPORTANCE_HIGH
        ).apply {
            description = NotificationConstants.CHANNEL_DESCRIPTION
            enableLights(true)
            lightColor = Color.GREEN
        }

        notificationManager.createNotificationChannel(channel)

    }
}