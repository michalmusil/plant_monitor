package cz.mendelu.xmusil5.plantmonitor.di

import android.content.Context
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.ISettingsDataStore
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.SettingsDataStoreMock
import cz.mendelu.xmusil5.plantmonitor.datastore.user_login.IUserLoginDataStore
import cz.mendelu.xmusil5.plantmonitor.datastore.user_login.UserLogindataStoreMock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
class MockDataStoreModule {
    @Provides
    @ActivityRetainedScoped
    fun provideSettingsDataStore(@ApplicationContext context: Context): ISettingsDataStore{
        return SettingsDataStoreMock(context)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideUserLoginDataStore(): IUserLoginDataStore{
        return UserLogindataStoreMock()
    }
}