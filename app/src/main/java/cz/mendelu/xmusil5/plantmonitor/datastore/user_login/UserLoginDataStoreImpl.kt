package cz.mendelu.xmusil5.plantmonitor.datastore.user_login

import android.content.Context
import androidx.datastore.preferences.core.*
import cz.mendelu.xmusil5.plantmonitor.datastore.DataStoreConstants
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role
import cz.mendelu.xmusil5.plantmonitor.utils.userLoginDataStore
import kotlinx.coroutines.flow.firstOrNull

class UserLoginDataStoreImpl(
    val context: Context
): IUserLoginDataStore {
    override suspend fun getSavedUserLogin(): User? {
        val preference = context.userLoginDataStore.data.firstOrNull()
        preference?.let {
            val userId = it.get(longPreferencesKey(DataStoreConstants.USER_ID_KEY))
            val email = it.get(stringPreferencesKey(DataStoreConstants.USER_EMAIL_KEY))
            val role = Role.getByRoleNumber(
                roleNumber = it.get(intPreferencesKey(DataStoreConstants.USER_ROLE_KEY)) ?: Int.MIN_VALUE
            )
            val token = it.get(stringPreferencesKey(DataStoreConstants.USER_TOKEN_KEY))
            if (userId != null && email != null && role != null && token != null){
                return User(
                    userId = userId,
                    email = email,
                    role = role,
                    token = token
                )
            } else {
                removeUserLogin()
                return null
            }
        }
        removeUserLogin()
        return null
    }

    override suspend fun saveUserLogin(user: User) {
        context.userLoginDataStore.edit {
            it[longPreferencesKey(DataStoreConstants.USER_ID_KEY)] = user.userId
            it[stringPreferencesKey(DataStoreConstants.USER_EMAIL_KEY)] = user.email
            it[intPreferencesKey(DataStoreConstants.USER_ROLE_KEY)] = user.role.roleNumber
            it[stringPreferencesKey(DataStoreConstants.USER_TOKEN_KEY)] = user.token
        }
    }

    override suspend fun removeUserLogin() {
        context.userLoginDataStore.edit {
            it.remove(longPreferencesKey(DataStoreConstants.USER_ID_KEY))
            it.remove(stringPreferencesKey(DataStoreConstants.USER_EMAIL_KEY))
            it.remove(intPreferencesKey(DataStoreConstants.USER_ROLE_KEY))
            it.remove(stringPreferencesKey(DataStoreConstants.USER_TOKEN_KEY))
        }
    }

}