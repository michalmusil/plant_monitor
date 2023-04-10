package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices.DevicesRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements.IMeasurementsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements.MeasurementsRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes.IPlantNotesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes.PlantNotesRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.PlantsRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth.IUserAuthRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth.UserAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class RepositoryModule {

    @ActivityRetainedScoped
    @Provides
    fun provideUserAuthRepository(
        userSessionManager: IUserSessionManager,
        api: HousePlantMeasurementsApi
    ): IUserAuthRepository {
        return UserAuthRepositoryImpl(userSessionManager, api)
    }

    @ActivityRetainedScoped
    @Provides
    fun providePlantsRepository(
        userSessionManager: IUserSessionManager,
        api: HousePlantMeasurementsApi
    ): IPlantsRepository {
        return PlantsRepositoryImpl(userSessionManager, api)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideMeasurementsRepository(
        userSessionManager: IUserSessionManager,
        api: HousePlantMeasurementsApi
    ): IMeasurementsRepository {
        return MeasurementsRepositoryImpl(userSessionManager, api)
    }

    @ActivityRetainedScoped
    @Provides
    fun providePlantNotesRepository(
        userSessionManager: IUserSessionManager,
        api: HousePlantMeasurementsApi
    ): IPlantNotesRepository {
        return PlantNotesRepositoryImpl(userSessionManager, api)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDevicesRepository(
        userSessionManager: IUserSessionManager,
        api: HousePlantMeasurementsApi
    ): IDevicesRepository {
        return DevicesRepositoryImpl(userSessionManager, api)
    }
}