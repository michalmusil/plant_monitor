package cz.mendelu.xmusil5.plantmonitor.communication.utils

import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.CommunicationConstants
import retrofit2.Response

abstract class BaseApiRepository(
    protected val authenticationManager: IAuthenticationManager
) {

    fun <T: Any> processResponse(call: Response<T>): CommunicationResult<T> {
        if (!isAuthorized(call)){
            authenticationManager.logOut()
        }

        try {
            if (call.isSuccessful) {
                call.body()?.let {
                    return CommunicationResult.Success(it)
                } ?: kotlin.run {
                    return CommunicationResult.Error(
                        CommunicationError(
                            call.code(),
                            call.errorBody().toString()
                        )
                    )
                }
            } else {
                return CommunicationResult.Error(
                    CommunicationError(
                        call.code(),
                        call.errorBody().toString()
                    )
                )
            }

        } catch (ex: Exception) {
            return CommunicationResult.Exception(ex)
        }
    }

    fun <T: Any> processEmptyResponse(call: Response<T>): CommunicationResult<String> {
        if (!isAuthorized(call)){
            authenticationManager.logOut()
            authenticationManager.setUser(null)
        }

        if (call.isSuccessful) {
            return CommunicationResult.Success("")
        } else {
            return CommunicationResult.Error(
                CommunicationError(
                    call.code(),
                    call.errorBody().toString()
                )
            )
        }
    }

    fun <T: Any> isAuthorized(call: Response<T>): Boolean {
        return call.code() != CommunicationConstants.HOUSE_PLANT_MEASUREMENTS_API_UNAUTHORIZED_CODE
    }
}