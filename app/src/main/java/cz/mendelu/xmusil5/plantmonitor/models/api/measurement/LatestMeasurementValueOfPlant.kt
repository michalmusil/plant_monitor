package cz.mendelu.xmusil5.plantmonitor.models.api.measurement

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType

@JsonClass(generateAdapter = true)
data class LatestMeasurementValueOfPlant(
    @Json(name = "type") val measurementType: MeasurementType,
    @Json(name = "value") val value: Double,
    @Json(name = "measurementId") val measurementId: Long,
    @Json(name = "taken") val datetime: DateTimeFromApi?,
)