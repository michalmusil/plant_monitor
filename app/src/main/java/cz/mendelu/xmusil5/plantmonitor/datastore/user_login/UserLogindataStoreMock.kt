package cz.mendelu.xmusil5.plantmonitor.datastore.user_login

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser

class UserLogindataStoreMock: IUserLoginDataStore {
    override suspend fun getSavedUserLogin(): GetUser? {
        return null
    }

    override suspend fun saveUserLogin(user: GetUser) {
        // do nothing
    }

    override suspend fun removeUserLogin() {
        // do nothing
    }
}