package cz.mendelu.xmusil5.plantmonitor.datastore.settings

interface ISettingsDataStore {

    suspend fun areNotificationsEnabled(): Boolean

    suspend fun setNotificationsEnabled(enabled: Boolean)
}