package cz.mendelu.xmusil5.plantmonitor.communication.api

import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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
    @GET("users/{id}")
    suspend fun getUserByIdResponse(
        @Path("id")id: Long,
        @Header("Authorization") bearerToken: String
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @PUT("users/notificationToken")
    suspend fun updateNotificationToken(
        @Body putNotificationToken: PutNotificationTokenUpdate,
        @Header("Authorization") bearerToken: String
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
    @PUT("plants")
    suspend fun updatePlant(
        @Body putPlant: PutPlant,
        @Header("Authorization") bearerToken: String
    ): Response<GetPlant>

    @Headers("Content-Type: application/json")
    @DELETE("plants/{id}")
    suspend fun deletePlant(
        @Path("id") id: Long,
        @Header("Authorization") bearerToken: String
    ): Response<Unit>

    @GET("plants/images/{plantId}")
    suspend fun getPlantImage(
        @Path("plantId")plantId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<ResponseBody>

    @Multipart
    @PUT("plants/images")
    suspend fun uploadPlantImage(
        @Query("plantId") plantId: Long,
        @Part image: MultipartBody.Part,
        @Header("Authorization") bearerToken: String
    ): Response<GetPlant>






    @Headers("Content-Type: application/json")
    @GET("devices/user/{id}")
    suspend fun getAllDevices(
        @Path("id")userId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<List<GetDevice>>

    @Headers("Content-Type: application/json")
    @GET("devices/{id}")
    suspend fun getDeviceById(
        @Path("id")id: Long,
        @Header("Authorization") bearerToken: String
    ): Response<GetDevice>

    @Headers("Content-Type: application/json")
    @PUT("devices/register")
    suspend fun registerDevice(
        @Body putDeviceRegister: PutDeviceRegister,
        @Header("Authorization") bearerToken: String
    ): Response<GetDevice>

    @Headers("Content-Type: application/json")
    @PUT("devices/unregister/{id}")
    suspend fun unregisterDevice(
        @Path("id")deviceId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @PUT("devices/activation")
    suspend fun deviceActivation(
        @Body putDeviceActivation: PutDeviceActivation,
        @Header("Authorization") bearerToken: String
    ): Response<GetDevice>

    @Headers("Content-Type: application/json")
    @PUT("devices/assignToPlant")
    suspend fun devicePlantAssign(
        @Body putDevicePlantAssignment: PutDevicePlantAssignment,
        @Header("Authorization") bearerToken: String
    ): Response<GetDevice>






    @Headers("Content-Type: application/json")
    @GET("measurements/plant/{id}")
    suspend fun getMeasurementsOfPlant(
        @Path("id")plantId: Long,
        @Query("from")from: String?,
        @Query("to")to: String?,
        @Header("Authorization") bearerToken: String
    ): Response<List<GetMeasurement>>

    @Headers("Content-Type: application/json")
    @GET("measurements/plant/latestValues/{id}")
    suspend fun getLatestPlantMeasurementValues(
        @Path("id")plantId: Long,
        @Header("Authorization") bearerToken: String
    ): Response<List<LatestMeasurementValueOfPlant>>


}