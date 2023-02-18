package cz.mendelu.xmusil5.plantmonitor.authentication

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser

interface IAuthenticationManager {
    fun getUser(): GetUser?
    fun setUser(user: GetUser?)
    fun getUserId(): Long
    fun getToken(): String
    fun isLoggedIn(): Boolean

    fun logOut()
}