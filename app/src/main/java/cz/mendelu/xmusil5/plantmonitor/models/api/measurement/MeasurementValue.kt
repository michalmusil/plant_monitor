package cz.mendelu.xmusil5.plantmonitor.models.api.measurement

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi

@JsonClass(generateAdapter = true)
data class MeasurementValue(
    @Json(name = "type") val measurementType: MeasurementType,
    @Json(name = "value") val value: Double,
){
    @Transient var measurementDate: DateTimeFromApi? = null
}
