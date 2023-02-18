package cz.mendelu.xmusil5.plantmonitor.communication.api

import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import retrofit2.Response
import retrofit2.http.*


interface HousePlantMeasurementsApi {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(
        @Body postAuth: PostAuth
    ): Response<GetUser>

    @Headers("Content-Type: application/json")
    @POST("users/register")
    suspend fun register(
        @Body postAuth: PostAuth
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @GET("plants/user/{id}")
    suspend fun getAllPlants(
        @Path("id")userId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<List<GetPlant>>

    @Headers("Content-Type: application/json")
    @GET("devices/user/{id}")
    suspend fun getAllDevices(
        @Path("id")userId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<List<GetDevice>>

    @Headers("Content-Type: application/json")
    @GET("measurements/plant/{id}")
    suspend fun getMeasurementsOfPlant(
        @Path("id")plantId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<List<GetMeasurement>>

    @Headers("Content-Type: application/json")
    @GET("measurements/device/{id}")
    suspend fun getMeasurementsOfDevice(
        @Path("id")deviceId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<List<GetMeasurement>>
}