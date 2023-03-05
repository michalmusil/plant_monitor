package cz.mendelu.xmusil5.plantmonitor.models.api.device

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser

@JsonClass(generateAdapter = true)
data class GetDevice(
    @Json(name = "id") val id: Long,
    @Json(name = "communicationIdentifier") val communicationId: String,
    @Json(name = "isActive") val active: Boolean,
    @Json(name = "userId") val userId: Long?,
    @Json(name = "plantId") val plantId: Long?,
){
    @Transient var plant: GetPlant? = null
    @Transient var user: GetUser? = null
}
