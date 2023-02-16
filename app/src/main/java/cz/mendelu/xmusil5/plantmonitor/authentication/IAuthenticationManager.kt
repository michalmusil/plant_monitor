package cz.mendelu.xmusil5.plantmonitor.authentication

import cz.mendelu.xmusil5.plantmonitor.models.api.User

interface IAuthenticationManager {
    fun getUser(): User?
    fun setUser(user: User?)
    fun getToken(): String
    fun isLoggedIn(): Boolean

    fun logOut()
}