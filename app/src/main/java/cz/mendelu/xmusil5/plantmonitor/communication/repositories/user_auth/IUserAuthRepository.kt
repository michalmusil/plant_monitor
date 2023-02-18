package cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth

interface IUserAuthRepository {

    suspend fun login(postAuth: PostAuth): CommunicationResult<GetUser>

    suspend fun register(postAuth: PostAuth): CommunicationResult<Unit>
}