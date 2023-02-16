package cz.mendelu.xmusil5.plantmonitor.models.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name="id")val userId: Long,
    @Json(name="username")val userName: String,
    @Json(name="role")var role: Int,
    @Json(name="token")var token: String
)