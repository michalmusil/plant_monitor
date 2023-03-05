package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate

interface IUserAuthRepository {

    suspend fun login(postAuth: PostAuth): CommunicationResult<GetUser>

    suspend fun register(postAuth: PostAuth): CommunicationResult<Unit>

    suspend fun updateNotificationToken(putNotificationToken: PutNotificationTokenUpdate): CommunicationResult<Unit>
}