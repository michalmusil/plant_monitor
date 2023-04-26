package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth

import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate

interface IUserAuthRepository {

    suspend fun login(postAuth: PostAuth): DataResult<User>

    suspend fun register(postAuth: PostAuth): DataResult<Unit>

    suspend fun updateNotificationToken(putNotificationToken: PutNotificationTokenUpdate): DataResult<Unit>

    suspend fun checkCurrentSignedUserValid(): DataResult<Unit>
}