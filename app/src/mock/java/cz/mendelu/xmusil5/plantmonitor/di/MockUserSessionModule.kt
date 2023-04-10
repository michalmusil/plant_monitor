package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.user_session.UserSessionManagerMock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped


@Module
@InstallIn(ActivityRetainedComponent::class)
class MockUserSessionModule {

    @Provides
    @ActivityRetainedScoped
    fun provideUserSessionManager(navigationRouter: INavigationRouter): IUserSessionManager {
        return UserSessionManagerMock(navigationRouter = navigationRouter)
    }
}