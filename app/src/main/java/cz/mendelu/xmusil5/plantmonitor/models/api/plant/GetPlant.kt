package cz.mendelu.xmusil5.plantmonitor.models.api.plant

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit

@JsonClass(generateAdapter = true)
data class GetPlant(
    @Json(name = "id") val id: Long,
    @Json(name = "userId") val userId: Long?,
    @Json(name = "name") val name: String,
    @Json(name = "species") val species: String,
    @Json(name = "description") val description: String?,
    @Json(name = "measurementValueLimits") val valueLimits: List<MeasurementValueLimit>
)