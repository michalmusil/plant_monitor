package cz.mendelu.xmusil5.plantmonitor.datastore.settings

class SettingsDataStoreMock: ISettingsDataStore {

    var notificationsEnabled = true

    override suspend fun areNotificationsEnabled(): Boolean {
        return notificationsEnabled
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        notificationsEnabled = enabled
    }
}