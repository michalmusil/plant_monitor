package cz.mendelu.xmusil5.plantmonitor.models.api.user

import com.squareup.moshi.Json

data class PutNotificationTokenUpdate(
    @Json(name = "notificationToken") val notificationToken: String?
)
