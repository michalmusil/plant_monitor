package cz.mendelu.xmusil5.plantmonitor.models.api.device

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PutDevicePlantAssignment(
    @Json(name = "deviceId") val deviceId: Long,
    @Json(name = "plantId") val plantId: Long,
)
