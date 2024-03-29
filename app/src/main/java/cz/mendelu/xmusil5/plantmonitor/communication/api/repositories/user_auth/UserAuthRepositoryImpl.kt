package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth

import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate
import javax.inject.Inject

class UserAuthRepositoryImpl @Inject constructor(
    userSessionManager: IUserSessionManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(userSessionManager), IUserAuthRepository {

    override suspend fun login(postAuth: PostAuth): DataResult<User> {
        return processRequest{
            api.login(postAuth)
        }
    }

    override suspend fun register(postAuth: PostAuth): DataResult<Unit> {
        return processRequest {
            api.register(postAuth)
        }
    }

    override suspend fun updateNotificationToken(putNotificationToken: PutNotificationTokenUpdate): DataResult<Unit> {
        return processRequest {
            api.updateNotificationToken(
                putNotificationToken = putNotificationToken,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun checkCurrentSignedUserValid(): DataResult<Unit> {
        return processRequest {
            api.getUserByIdResponse(
                id = userSessionManager.getUserId(),
                bearerToken = userSessionManager.getToken()
            )
        }
    }

}