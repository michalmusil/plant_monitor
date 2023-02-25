package cz.mendelu.xmusil5.plantmonitor.models.api.device

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostDeviceRegister(
    @Json(name = "uuid") val uuid: String,
    @Json(name = "macAddress") val macAddress: String,
)
