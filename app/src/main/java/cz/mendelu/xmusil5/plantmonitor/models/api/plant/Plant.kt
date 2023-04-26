package cz.mendelu.xmusil5.plantmonitor.models.api.plant

import android.graphics.Bitmap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi

@JsonClass(generateAdapter = true)
data class Plant(
    @Json(name = "id") val id: Long,
    @Json(name = "userId") val userId: Long?,
    @Json(name = "name") val name: String,
    @Json(name = "species") val species: String,
    @Json(name = "description") val description: String?,
    @Json(name = "hasTitleImage") val hasTitleImage: Boolean,
    @Json(name = "created") val created: DateTimeFromApi?,
    @Json(name = "measurementValueLimits") val valueLimits: List<MeasurementValueLimit>
){

    @Transient var titleImageBitmap: Bitmap? = null

    @Transient var mostRecentValues: List<LatestMeasurementValueOfPlant>? = null

    @Transient var associatedDevices: List<Device>? = null
}
