package cz.mendelu.xmusil5.plantmonitor.datastore.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import cz.mendelu.xmusil5.plantmonitor.utils.settingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SettingsDataStoreImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
): ISettingsDataStore {
    companion object {
        private const val defaultNotificationsEnabled: Boolean = true
    }
    init {
        coroutineScope.launch {
            darkModePreference?.value = getDarkModePreference()
        }
    }

    override val darkModePreference = MutableStateFlow<Boolean?>(null)

    private fun getNotificationsEnabledKey(user: User): String{
        return "NotifyUserId:${user.userId}"
    }
    private fun getDarkModePreferenceKey(): String{
        return "PrefersDarkMode"
    }
    private fun getAppLanguagePreferenceKey(user: User): String{
        return "LanguageOfUserId:${user.userId}"
    }


    override suspend fun areNotificationsEnabled(user: User): Boolean {
        val preference = context.settingsDataStore.data.firstOrNull()
        val key = getNotificationsEnabledKey(user)
        return preference?.get(booleanPreferencesKey(key))
            ?: defaultNotificationsEnabled
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean, user: User) {
        val key = getNotificationsEnabledKey(user)
        context.settingsDataStore.edit {
            it[booleanPreferencesKey(key)] = enabled
        }
    }

    override suspend fun getDarkModePreference(): Boolean? {
        val preference = context.settingsDataStore.data.firstOrNull()
        val key = getDarkModePreferenceKey()
        return preference?.get(booleanPreferencesKey(key))
    }

    override suspend fun setDarkModePreference(darkMode: Boolean?) {
        val key = getDarkModePreferenceKey()
        context.settingsDataStore.edit {
            if (darkMode != null){
                it[booleanPreferencesKey(key)] = darkMode
            } else {
                it.remove(booleanPreferencesKey(key))
            }
        }
        darkModePreference.value = darkMode
    }
}