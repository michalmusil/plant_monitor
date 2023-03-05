package cz.mendelu.xmusil5.plantmonitor.models.api.device

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PutDeviceActivation(
    @Json(name = "deviceId") val deviceId: Long,
    @Json(name = "isActive") val isActive: Boolean,
)
