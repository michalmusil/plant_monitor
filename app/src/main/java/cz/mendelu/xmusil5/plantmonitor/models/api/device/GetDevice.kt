package cz.mendelu.xmusil5.plantmonitor.models.api.device

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetDevice(
    @Json(name = "id") val id: Long,
    @Json(name = "uuid") val uuid: String,
    @Json(name = "isActive") val active: Boolean,
    @Json(name = "userId") val userId: Long,
    @Json(name = "plantId") val plantId: Long,
)