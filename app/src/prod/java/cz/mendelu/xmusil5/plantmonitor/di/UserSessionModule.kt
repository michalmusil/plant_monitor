package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.user_session.UserSessionManagerImpl
import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.datastore.user_login.IUserLoginDataStore
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class UserSessionModule {

    @Provides
    @ActivityRetainedScoped
    fun provideUserSessionManager(navigationRouter: INavigationRouter, userLoginDataStore: IUserLoginDataStore): IUserSessionManager {
        return UserSessionManagerImpl(
            navigationRouter = navigationRouter,
            userLoginDataStore = userLoginDataStore
        )
    }
}