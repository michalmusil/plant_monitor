package cz.mendelu.xmusil5.plantmonitor.communication.utils

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.ApiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageUtils
import okhttp3.ResponseBody
import retrofit2.Response


abstract class BaseApiRepository(
    protected val userSessionManager: IUserSessionManager
) {
    inline fun <T: Any> processRequest(request: () -> Response<T>): DataResult<T> {
        try {
            val response = request.invoke()
            if (!isAuthorized(response)){
                logOut()
            }
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null){
                    return DataResult.Success(body)
                } else {
                    return DataResult.Error(
                        error = DataError(
                            code = response.code(),
                            message = response.errorBody().toString()
                        )
                    )
                }
            } else {
                return DataResult.Error(
                    error = DataError(
                        code = response.code(),
                        message = response.errorBody().toString()
                    )
                )
            }
        } catch (ex: Exception){
            return DataResult.Exception(exception = ex)
        }
    }

    inline fun processImageRequest(
        resultBitmapQuality: ImageQuality,
        request: () -> Response<ResponseBody>
    ): DataResult<Bitmap>{
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
                        return DataResult.Success(data = bitmap)
                    }
                    return DataResult.Exception(NullPointerException())
                }
                return DataResult.Error(
                    DataError(
                        response.code(),
                        response.errorBody().toString()
                    )
                )
            } else {
                return DataResult.Error(
                    DataError(
                        response.code(),
                        response.errorBody().toString()
                    )
                )
            }
        }catch(ex: Exception){
            return DataResult.Exception(ex)
        }
    }

    fun <T: Any> isAuthorized(call: Response<T>): Boolean {
        return call.code() != ApiConstants.HOUSE_PLANT_MEASUREMENTS_API_UNAUTHORIZED_CODE
    }

    fun logOut(){
        userSessionManager.logOut()
        userSessionManager.setUser(null)
    }
}