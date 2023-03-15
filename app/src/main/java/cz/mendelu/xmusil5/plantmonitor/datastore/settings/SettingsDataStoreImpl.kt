package cz.mendelu.xmusil5.plantmonitor.datastore.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import cz.mendelu.xmusil5.plantmonitor.datastore.DataStoreConstants
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.utils.settingsDataStore
import kotlinx.coroutines.flow.firstOrNull

class SettingsDataStoreImpl(
    private val context: Context
): ISettingsDataStore {

    companion object {
        private const val defaultNotificationsEnabled: Boolean = true
    }

    private fun getNotificationsEnabledKey(user: GetUser): String{
        return "NotifyUserId:${user.userId}"
    }


    override suspend fun areNotificationsEnabled(user: GetUser): Boolean {
        val preference = context.settingsDataStore.data.firstOrNull()
        val key = getNotificationsEnabledKey(user)
        return preference?.get(booleanPreferencesKey(key))
            ?: defaultNotificationsEnabled
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean, user: GetUser) {
        val key = getNotificationsEnabledKey(user)
        context.settingsDataStore.edit {
            it[booleanPreferencesKey(key)] = enabled
        }
    }
}