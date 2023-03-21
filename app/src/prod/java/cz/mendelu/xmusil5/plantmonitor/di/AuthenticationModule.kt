package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.authentication.AuthenticationManagerImpl
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.datastore.user_login.IUserLoginDataStore
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
class AuthenticationModule {

    @Provides
    @ActivityRetainedScoped
    fun provideAuthenticationManager(navigationRouter: INavigationRouter, userLoginDataStore: IUserLoginDataStore): IAuthenticationManager {
        return AuthenticationManagerImpl(
            navigationRouter = navigationRouter,
            userLoginDataStore = userLoginDataStore
        )
    }
}