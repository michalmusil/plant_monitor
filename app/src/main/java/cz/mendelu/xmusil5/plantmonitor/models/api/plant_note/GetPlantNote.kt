package cz.mendelu.xmusil5.plantmonitor.models.api.plant_note

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi

@JsonClass(generateAdapter = true)
data class GetPlantNote(
    @Json(name = "id") val id: Long,
    @Json(name = "text") val text: String,
    @Json(name = "plantId") val plantId: Long,
    @Json(name = "created") val created: DateTimeFromApi?,
)
