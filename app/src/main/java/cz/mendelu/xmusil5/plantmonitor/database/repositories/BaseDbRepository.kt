package cz.mendelu.xmusil5.plantmonitor.database.repositories

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult

abstract class BaseDbRepository {

    inline fun <T: Any> processDbCall(dbCall: () -> T): CommunicationResult<T> {
        try {
            val result = dbCall.invoke()
            if (result == null){
                return CommunicationResult.Error(
                    CommunicationError(
                        code = 404,
                        message = "Not found"
                    )
                )
            }
            return CommunicationResult.Success(data = result)

        }catch (ex: Exception){
            return CommunicationResult.Exception(ex)
        }
    }
}