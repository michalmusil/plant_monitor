package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth

import cz.mendelu.xmusil5.plantmonitor.authentication.AuthenticationManagerMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate
import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role

class UserAuthRepositoryMock: IUserAuthRepository {

    override suspend fun login(postAuth: PostAuth): CommunicationResult<GetUser> {
        val newUser = GetUser(
            userId = AuthenticationManagerMock.MOCKED_USER_ID,
            email = postAuth.email,
            role = Role.USER,
            token = AuthenticationManagerMock.MOCKED_USER_TOKEN
        )
        return CommunicationResult.Success(data = newUser)
    }

    override suspend fun register(postAuth: PostAuth): CommunicationResult<Unit> {
        return CommunicationResult.Success(data = Unit)
    }

    override suspend fun updateNotificationToken(putNotificationToken: PutNotificationTokenUpdate): CommunicationResult<Unit> {
        return CommunicationResult.Success(data = Unit)
    }

    override suspend fun checkCurrentSignedUserValid(): CommunicationResult<Unit> {
        return CommunicationResult.Success(Unit)
    }

}