package cz.mendelu.xmusil5.plantmonitor.datastore.settings

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser

class SettingsDataStoreMock: ISettingsDataStore {

    var notificationsEnabled = true

    override suspend fun areNotificationsEnabled(user: GetUser): Boolean {
        return notificationsEnabled
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean, user: GetUser) {
        notificationsEnabled = enabled
    }
}