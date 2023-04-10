package cz.mendelu.xmusil5.plantmonitor.datastore.settings

import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import kotlinx.coroutines.flow.MutableStateFlow

interface ISettingsDataStore {
    val darkModePreference: MutableStateFlow<Boolean?>

    suspend fun areNotificationsEnabled(user: User): Boolean
    suspend fun setNotificationsEnabled(enabled: Boolean, user: User)

    suspend fun getDarkModePreference(): Boolean?
    suspend fun setDarkModePreference(darkMode: Boolean?)
}