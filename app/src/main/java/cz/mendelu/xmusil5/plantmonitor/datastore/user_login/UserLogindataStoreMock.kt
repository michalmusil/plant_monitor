package cz.mendelu.xmusil5.plantmonitor.datastore.user_login

import cz.mendelu.xmusil5.plantmonitor.models.api.user.User

class UserLogindataStoreMock: IUserLoginDataStore {
    override suspend fun getSavedUserLogin(): User? {
        return null
    }

    override suspend fun saveUserLogin(user: User) {
        // do nothing
    }

    override suspend fun removeUserLogin() {
        // do nothing
    }
}