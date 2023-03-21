package cz.mendelu.xmusil5.plantmonitor.datastore.user_login

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser

interface IUserLoginDataStore {
    suspend fun getSavedUserLogin(): GetUser?
    suspend fun saveUserLogin(user: GetUser)
    suspend fun removeUserLogin()
}