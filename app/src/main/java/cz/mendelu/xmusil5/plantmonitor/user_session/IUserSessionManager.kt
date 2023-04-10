package cz.mendelu.xmusil5.plantmonitor.user_session

import cz.mendelu.xmusil5.plantmonitor.models.api.user.User

interface IUserSessionManager {
    fun getUser(): User?
    fun setUser(user: User?)
    fun getUserId(): Long
    fun getToken(): String
    fun isLoggedIn(): Boolean

    fun logOut()
}