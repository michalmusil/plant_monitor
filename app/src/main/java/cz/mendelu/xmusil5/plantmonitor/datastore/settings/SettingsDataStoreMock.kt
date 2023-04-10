package cz.mendelu.xmusil5.plantmonitor.datastore.settings

import android.content.Context
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsDataStoreMock(
    private val context: Context,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
): ISettingsDataStore {

    var notificationsEnabled = true
    var darkModePreferenceTemp: Boolean? = null

    override val darkModePreference =  MutableStateFlow<Boolean?>(null)

    init {
        coroutineScope.launch {
            darkModePreference.value = getDarkModePreference()
        }
    }

    override suspend fun areNotificationsEnabled(user: User): Boolean {
        return notificationsEnabled
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean, user: User) {
        notificationsEnabled = enabled
    }

    override suspend fun getDarkModePreference(): Boolean? {
        return darkModePreferenceTemp
    }

    override suspend fun setDarkModePreference(darkMode: Boolean?) {
        darkModePreferenceTemp = darkMode
        darkModePreference.value = darkMode
    }
}