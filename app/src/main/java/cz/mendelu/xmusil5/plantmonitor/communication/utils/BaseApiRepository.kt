package cz.mendelu.xmusil5.plantmonitor.communication.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.ApiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageUtils
import okhttp3.ResponseBody
import retrofit2.Response


abstract class BaseApiRepository(
    protected val authenticationManager: IAuthenticationManager
) {

    inline fun <T: Any> processRequest(request: () -> Response<T>): CommunicationResult<T> {
        try {
            val response = request.invoke()
            if (!isAuthorized(response)){
                logOut()
            }
            if (response.isSuccessful) {
                response.body()?.let {
                    return CommunicationResult.Success(it)
                } ?: kotlin.run {
                    return CommunicationResult.Error(
                        CommunicationError(
                            response.code(),
                            response.errorBody().toString()
                        )
                    )
                }
            } else {
                return CommunicationResult.Error(
                    CommunicationError(
                        response.code(),
                        response.errorBody().toString()
                    )
                )
            }
        }catch (ex: Exception){
            return CommunicationResult.Exception(ex)
        }
    }

    inline fun processImageRequest(
        resultBitmapQuality: ImageQuality,
        request: () -> Response<ResponseBody>
    ): CommunicationResult<Bitmap>{
        try {
            val response = request.invoke()

            if (!isAuthorized(response)){
                logOut()
            }

            if (response.isSuccessful()) {
                response.body()?.let {
                    val bitmap = ImageUtils.getBitmapFromInputStream(
                        inputStream = it.byteStream(),
                        quality = resultBitmapQuality
                    )
                    bitmap?.let {
                        return CommunicationResult.Success(data = bitmap)
                    }
                    return CommunicationResult.Exception(NullPointerException())
                }
                return CommunicationResult.Error(
                    CommunicationError(
                        response.code(),
                        response.errorBody().toString()
                    )
                )
            } else {
                return CommunicationResult.Error(
                    CommunicationError(
                        response.code(),
                        response.errorBody().toString()
                    )
                )
            }
        }catch(ex: Exception){
            return CommunicationResult.Exception(ex)
        }
    }

    fun <T: Any> isAuthorized(call: Response<T>): Boolean {
        return call.code() != ApiConstants.HOUSE_PLANT_MEASUREMENTS_API_UNAUTHORIZED_CODE
    }

    fun logOut(){
        authenticationManager.logOut()
        authenticationManager.setUser(null)
    }
}