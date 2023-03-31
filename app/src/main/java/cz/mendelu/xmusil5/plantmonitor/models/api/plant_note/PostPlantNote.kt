package cz.mendelu.xmusil5.plantmonitor.models.api.plant_note

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostPlantNote(
    @Json(name = "text") val text: String,
    @Json(name = "plantId") val plantId: Long
)
