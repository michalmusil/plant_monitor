package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager.INotificationTokenManager
import cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager.NotificationTokenManagerMock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class MockManagerModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNotificationManager(): INotificationTokenManager{
        return NotificationTokenManagerMock()
    }
}