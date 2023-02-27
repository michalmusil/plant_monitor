package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.authentication.AuthenticationManagerMock
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class MockAuthenticationModule {

    @Provides
    @ActivityRetainedScoped
    fun provideAuthenticationManager(navigationRouter: INavigationRouter): IAuthenticationManager {
        return AuthenticationManagerMock(navigationRouter = navigationRouter)
    }
}