package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.database.daos.*
import cz.mendelu.xmusil5.plantmonitor.database.repositories.devices.DbDevicesRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.database.repositories.devices.IDbDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.database.repositories.measurements.DbMeasurementsRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.database.repositories.measurements.IDbMeasurementsRepository
import cz.mendelu.xmusil5.plantmonitor.database.repositories.plant_notes.DbPlantNotesRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.database.repositories.plant_notes.IDbPlantNotesRepository
import cz.mendelu.xmusil5.plantmonitor.database.repositories.plants.DbPlantsRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.database.repositories.plants.IDbPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.database.repositories.users.DbUsersRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.database.repositories.users.IDbUsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DbRepositoryModule {
    @ActivityRetainedScoped
    @Provides
    fun provideDbPlantsRepository(
        plantsDao: PlantsDao
    ): IDbPlantsRepository {
        return DbPlantsRepositoryImpl(plantsDao)
    }
    @ActivityRetainedScoped
    @Provides
    fun provideDbDevicesRepository(
        devicesDao: DevicesDao
    ): IDbDevicesRepository {
        return DbDevicesRepositoryImpl(devicesDao)
    }
    @ActivityRetainedScoped
    @Provides
    fun provideDbPlantNotesRepository(
        plantNotesDao: PlantNotesDao
    ): IDbPlantNotesRepository {
        return DbPlantNotesRepositoryImpl(plantNotesDao)
    }
    @ActivityRetainedScoped
    @Provides
    fun provideDbMeasurementsRepository(
        measurementsDao: MeasurementsDao
    ): IDbMeasurementsRepository {
        return DbMeasurementsRepositoryImpl(measurementsDao)
    }
    @ActivityRetainedScoped
    @Provides
    fun provideDbUsersRepository(
        usersDao: UsersDao
    ): IDbUsersRepository {
        return DbUsersRepositoryImpl(usersDao)
    }
}