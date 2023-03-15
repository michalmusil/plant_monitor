package cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager

class NotificationTokenManagerMock: INotificationTokenManager {
    override suspend fun getNotificationToken(): String? {
        return "1234567890"
    }
}