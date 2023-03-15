package cz.mendelu.xmusil5.plantmonitor.models.api.plant

import android.graphics.Bitmap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit

@JsonClass(generateAdapter = true)
data class GetPlant(
    @Json(name = "id") val id: Long,
    @Json(name = "userId") val userId: Long?,
    @Json(name = "name") val name: String,
    @Json(name = "species") val species: String,
    @Json(name = "description") val description: String?,
    @Json(name = "hasTitleImage") val hasTitleImage: Boolean,
    @Json(name = "measurementValueLimits") val valueLimits: List<MeasurementValueLimit>
){

    @Transient var titleImageBitmap: Bitmap? = null

    @Transient var mostRecentValues: List<MeasurementValue>? = null

    @Transient var associatedDevice: GetDevice? = null
}
