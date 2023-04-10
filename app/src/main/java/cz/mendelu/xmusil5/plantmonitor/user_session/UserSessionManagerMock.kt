package cz.mendelu.xmusil5.plantmonitor.user_session

import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import javax.inject.Inject

class UserSessionManagerMock @Inject constructor(
    private val navigationRouter: INavigationRouter
): IUserSessionManager {

    companion object{
        val MOCKED_USER_ID = 1L
        val MOCKED_USER_TOKEN = "hqwfoiuhq3o48f7hq3o874hvqo8ayu34gco872g34fo8q7wg"
    }

    private var loggedUser: User? = null

    override fun getUser(): User? {
        if (loggedUser == null){
            return null
        }
        return User(
            userId = MOCKED_USER_ID,
            email = loggedUser!!.email,
            role = Role.USER,
            token = MOCKED_USER_TOKEN
        )
    }

    override fun setUser(user: User?) {
        this.loggedUser = user
    }

    override fun getUserId(): Long {
        return MOCKED_USER_ID
    }

    override fun getToken(): String {
        return "Bearer ${MOCKED_USER_TOKEN}"
    }

    override fun isLoggedIn(): Boolean {
        return loggedUser != null
    }

    override fun logOut() {
        setUser(null)
        navigationRouter.toLogin()
    }
}