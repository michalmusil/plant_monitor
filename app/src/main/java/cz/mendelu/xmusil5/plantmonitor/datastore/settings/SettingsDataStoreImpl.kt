package cz.mendelu.xmusil5.plantmonitor.datastore.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import cz.mendelu.xmusil5.plantmonitor.datastore.DataStoreConstants
import cz.mendelu.xmusil5.plantmonitor.utils.settingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class SettingsDataStoreImpl(
    private val context: Context
): ISettingsDataStore {
    companion object {
        private const val defaultNotificationsEnabled: Boolean = true
    }


    override suspend fun areNotificationsEnabled(): Boolean {
        val preference = context.settingsDataStore.data.firstOrNull()
        return preference?.get(booleanPreferencesKey(DataStoreConstants.NOTIFICATIONS_ENABLED_KEY))
            ?: defaultNotificationsEnabled
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.settingsDataStore.edit {
            it[booleanPreferencesKey(DataStoreConstants.NOTIFICATIONS_ENABLED_KEY)] = enabled
        }
    }
}