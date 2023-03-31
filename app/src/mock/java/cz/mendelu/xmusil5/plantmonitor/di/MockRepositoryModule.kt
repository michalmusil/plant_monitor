package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices.DevicesRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements.IMeasurementsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements.MeasurementsRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes.IPlantNotesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes.PlantNotesRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.PlantsRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth.IUserAuthRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth.UserAuthRepositoryMock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped


@Module
@InstallIn(ActivityRetainedComponent::class)
class MockRepositoryModule {

    @ActivityRetainedScoped
    @Provides
    fun provideUserAuthRepository(): IUserAuthRepository {
        return UserAuthRepositoryMock()
    }

    @ActivityRetainedScoped
    @Provides
    fun providePlantsRepository(): IPlantsRepository {
        return PlantsRepositoryMock()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideMeasurementsRepository(): IMeasurementsRepository {
        return MeasurementsRepositoryMock()
    }

    @ActivityRetainedScoped
    @Provides
    fun providePlantNotesRepository(): IPlantNotesRepository {
        return PlantNotesRepositoryMock()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDevicesRepository(): IDevicesRepository {
        return DevicesRepositoryMock()
    }
}