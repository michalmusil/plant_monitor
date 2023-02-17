package cz.mendelu.xmusil5.plantmonitor.models.api.measurement

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MeasurementValueLimit(
    @Json(name = "type") val type: MeasurementType,
    @Json(name = "lowerLimit") val lowerLimit: Double,
    @Json(name = "upperLimit") val upperLimit: Double,
)
