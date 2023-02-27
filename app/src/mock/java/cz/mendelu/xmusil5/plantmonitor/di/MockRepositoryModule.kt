package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices.DevicesRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices.DevicesRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements.IMeasurementsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements.MeasurementsRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements.MeasurementsRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.PlantsRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.PlantsRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth.IUserAuthRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth.UserAuthRepositoryMock
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
    fun provideDevicesRepository(): IDevicesRepository {
        return DevicesRepositoryMock()
    }
}