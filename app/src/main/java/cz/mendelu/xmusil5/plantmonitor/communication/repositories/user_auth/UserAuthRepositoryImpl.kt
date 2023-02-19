package cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth

import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import javax.inject.Inject

class UserAuthRepositoryImpl @Inject constructor(
    authenticationManager: IAuthenticationManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(authenticationManager), IUserAuthRepository {

    override suspend fun login(postAuth: PostAuth): CommunicationResult<GetUser> {
        return processResponse(api.login(postAuth))
    }

    override suspend fun register(postAuth: PostAuth): CommunicationResult<Unit> {
        return processResponse(api.register(postAuth))
    }

}