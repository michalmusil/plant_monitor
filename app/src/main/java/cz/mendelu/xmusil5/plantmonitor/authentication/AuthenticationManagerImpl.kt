package cz.mendelu.xmusil5.plantmonitor.authentication

import cz.mendelu.xmusil5.plantmonitor.datastore.user_login.IUserLoginDataStore
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(
    private val navigationRouter: INavigationRouter,
    private val userLoginDataStore: IUserLoginDataStore,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
): IAuthenticationManager {

    private var loggedUser: GetUser? = null

    override fun getUser(): GetUser? {
        return loggedUser
    }

    override fun setUser(user: GetUser?) {
        this.loggedUser = user
    }

    override fun getUserId(): Long {
        return loggedUser?.userId ?: -1L
    }

    override fun getToken(): String {
        return "Bearer ${loggedUser?.token ?: ""}"
    }

    override fun isLoggedIn(): Boolean {
        return loggedUser != null
    }



    override fun logOut() {
        coroutineScope.launch {
            userLoginDataStore.removeUserLogin()
        }.invokeOnCompletion {
            CoroutineScope(Dispatchers.Main).launch {
                setUser(null)
                navigationRouter.toLogin()
            }
        }
    }
}