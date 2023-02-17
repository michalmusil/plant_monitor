package cz.mendelu.xmusil5.plantmonitor.authentication

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(
    private val navigationRouter: INavigationRouter
): IAuthenticationManager {

    private var loggedUser: GetUser? = null

    override fun getUser(): GetUser? {
        return loggedUser
    }

    override fun setUser(user: GetUser?) {
        this.loggedUser = user
    }

    override fun getToken(): String {
        return "Bearer ${loggedUser?.token ?: ""}"
    }

    override fun isLoggedIn(): Boolean {
        return loggedUser != null
    }



    override fun logOut() {
        setUser(null)
        navigationRouter.toLogin()
    }
}