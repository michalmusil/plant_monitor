package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager.INotificationTokenManager
import cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager.NotificationTokenManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class ManagerModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNotificationManager(): INotificationTokenManager{
        return NotificationTokenManagerImpl()
    }
}