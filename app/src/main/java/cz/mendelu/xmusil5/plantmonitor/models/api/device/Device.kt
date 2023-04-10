package cz.mendelu.xmusil5.plantmonitor.models.api.device

import android.content.Context
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User

@JsonClass(generateAdapter = true)
data class Device(
    @Json(name = "id") val id: Long,
    @Json(name = "isActive") val active: Boolean,
    @Json(name = "userId") val userId: Long?,
    @Json(name = "plantId") val plantId: Long?,
){
    @Transient var plant: Plant? = null
    @Transient var user: User? = null

    fun getDisplayName(context: Context): String{
        return "${context.getString(R.string.measuringDevice)} $id"
    }
}
