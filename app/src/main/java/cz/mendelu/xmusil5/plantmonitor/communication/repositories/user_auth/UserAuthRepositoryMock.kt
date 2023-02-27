package cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth

import cz.mendelu.xmusil5.plantmonitor.authentication.AuthenticationManagerMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
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

}