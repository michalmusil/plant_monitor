package cz.mendelu.xmusil5.plantmonitor.datastore.user_login

import cz.mendelu.xmusil5.plantmonitor.models.api.user.User

interface IUserLoginDataStore {
    suspend fun getSavedUserLogin(): User?
    suspend fun saveUserLogin(user: User)
    suspend fun removeUserLogin()
}