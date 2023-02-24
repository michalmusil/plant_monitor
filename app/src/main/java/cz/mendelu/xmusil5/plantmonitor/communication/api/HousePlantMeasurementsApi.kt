package cz.mendelu.xmusil5.plantmonitor.communication.api

import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import okhttp3.ResponseBody
import retrofit2.Call
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
    @GET("plants/{id}")
    suspend fun getPlantById(
        @Path("id")id: Long,
        @Header("Authorization") bearerToken: String
    ): Response<GetPlant>

    @Headers("Content-Type: application/json")
    @POST("plants")
    suspend fun postNewPlant(
        @Body postPlant: PostPlant,
        @Header("Authorization") bearerToken: String
    ): Response<GetPlant>

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
        @Query("from")from: String?,
        @Query("to")to: String?,
        @Header("Authorization") bearerToken: String
    ): Response<List<GetMeasurement>>

    @Headers("Content-Type: application/json")
    @GET("measurements/plant/latest/{id}")
    suspend fun getLatestPlantMeasurementOfType(
        @Path("id")plantId: Long,
        @Query("measurementType")measurementTypeNumber: Int,
        @Header("Authorization") bearerToken: String
    ): Response<GetMeasurement>

    @GET("plants/images/{plantId}")
    suspend fun getPlantImage(
        @Path("plantId")plantId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<ResponseBody>
}