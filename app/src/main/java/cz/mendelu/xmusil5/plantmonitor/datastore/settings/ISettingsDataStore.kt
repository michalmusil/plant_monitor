package cz.mendelu.xmusil5.plantmonitor.datastore.settings

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.utils.LanguageUtils
import kotlinx.coroutines.flow.MutableStateFlow

interface ISettingsDataStore {
    val darkModePreference: MutableStateFlow<Boolean?>

    suspend fun areNotificationsEnabled(user: GetUser): Boolean
    suspend fun setNotificationsEnabled(enabled: Boolean, user: GetUser)

    suspend fun getDarkModePreference(): Boolean?
    suspend fun setDarkModePreference(darkMode: Boolean?)
}