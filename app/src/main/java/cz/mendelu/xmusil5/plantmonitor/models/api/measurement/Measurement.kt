package cz.mendelu.xmusil5.plantmonitor.models.api.measurement

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType

@JsonClass(generateAdapter = true)
data class Measurement(
    @Json(name = "id") val id: Long,
    @Json(name = "taken") val datetime: DateTimeFromApi?,
    @Json(name = "plantId") val plantId: Long,
    @Json(name = "deviceId") val deviceId: Long,
    @Json(name = "measurementValues") val values: List<MeasurementValue>
){
    fun getMeasurementValueByType(measurementType: MeasurementType): MeasurementValue?{
        return values.firstOrNull { it.measurementType == measurementType }
    }
}
