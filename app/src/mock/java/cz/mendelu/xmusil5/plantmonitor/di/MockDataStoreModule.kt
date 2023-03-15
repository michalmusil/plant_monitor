package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.datastore.settings.ISettingsDataStore
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.SettingsDataStoreMock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
class MockDataStoreModule {
    @Provides
    @ActivityRetainedScoped
    fun provideSettingsDataStore(): ISettingsDataStore{
        return SettingsDataStoreMock()
    }
}