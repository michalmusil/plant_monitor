package cz.mendelu.xmusil5.plantmonitor.models.api.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostAuth(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)
