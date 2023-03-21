package cz.mendelu.xmusil5.plantmonitor.di

import android.content.Context
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.ISettingsDataStore
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.SettingsDataStoreImpl
import cz.mendelu.xmusil5.plantmonitor.datastore.user_login.IUserLoginDataStore
import cz.mendelu.xmusil5.plantmonitor.datastore.user_login.UserLoginDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DataStoreModule {
    @Provides
    @ActivityRetainedScoped
    fun provideSettingsDataStore(@ApplicationContext context: Context): ISettingsDataStore{
        return SettingsDataStoreImpl(context = context)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideUserLoginDataStore(@ApplicationContext context: Context): IUserLoginDataStore{
        return UserLoginDataStoreImpl(context)
    }
}