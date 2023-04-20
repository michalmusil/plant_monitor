package cz.mendelu.xmusil5.plantmonitor.di

import android.content.Context
import cz.mendelu.xmusil5.plantmonitor.database.PlantMonitorDatabase
import cz.mendelu.xmusil5.plantmonitor.database.daos.*
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.navigation.NavigationRouterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
class DatabaseModule {

    @Provides
    @ActivityRetainedScoped
    fun ProvidePlantMonitorDatabase(@ApplicationContext appContext: Context): PlantMonitorDatabase {
        return PlantMonitorDatabase.getDatabase(context = appContext)
    }

    @Provides
    @ActivityRetainedScoped
    fun ProvideDevicesDao(database: PlantMonitorDatabase): DevicesDao {
        return database.devicesDao()
    }
    @Provides
    @ActivityRetainedScoped
    fun ProvidePlantsDao(database: PlantMonitorDatabase): PlantsDao {
        return database.plantsDao()
    }
    @Provides
    @ActivityRetainedScoped
    fun ProvideUsersDao(database: PlantMonitorDatabase): UsersDao {
        return database.usersDao()
    }
    @Provides
    @ActivityRetainedScoped
    fun ProvideMeasurementsDao(database: PlantMonitorDatabase): MeasurementsDao {
        return database.measurementsDao()
    }
    @Provides
    @ActivityRetainedScoped
    fun ProvidePlantNotesDao(database: PlantMonitorDatabase): PlantNotesDao {
        return database.plantNotesDao()
    }
}