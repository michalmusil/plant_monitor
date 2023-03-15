package cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager

interface INotificationTokenManager {
    suspend fun getNotificationToken(): String?
}