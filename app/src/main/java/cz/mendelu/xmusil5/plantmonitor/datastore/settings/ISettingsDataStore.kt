package cz.mendelu.xmusil5.plantmonitor.datastore.settings

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser

interface ISettingsDataStore {

    suspend fun areNotificationsEnabled(user: GetUser): Boolean

    suspend fun setNotificationsEnabled(enabled: Boolean, user: GetUser)
}