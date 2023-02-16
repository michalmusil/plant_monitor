package cz.mendelu.xmusil5.plantmonitor.communication.api

import retrofit2.http.GET
import retrofit2.http.Headers


interface HousePlantMeasurementsApi {
    @Headers("Content-Type: application/json")
    @GET("authentication/login")
    suspend fun login()
}