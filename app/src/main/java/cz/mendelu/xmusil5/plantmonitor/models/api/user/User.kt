package cz.mendelu.xmusil5.plantmonitor.models.api.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name="id")val userId: Long,
    @Json(name="email")val email: String,
    @Json(name="role")var role: Role,
    @Json(name="token")var token: String
)
