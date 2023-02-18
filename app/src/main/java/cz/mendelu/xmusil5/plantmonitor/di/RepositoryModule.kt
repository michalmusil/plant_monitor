package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices.DevicesRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements.IMeasurementsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements.MeasurementsRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.PlantsRepositoryImpl
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth.IUserAuthRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth.UserAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class RepositoryModule {

    @Provides
    @ActivityScoped
    fun provideUserAuthRepository(
        authenticationManager: IAuthenticationManager,
        api: HousePlantMeasurementsApi
    ): IUserAuthRepository {
        return UserAuthRepositoryImpl(authenticationManager, api)
    }

    @Provides
    @ActivityScoped
    fun providePlantsRepository(
        authenticationManager: IAuthenticationManager,
        api: HousePlantMeasurementsApi
    ): IPlantsRepository {
        return PlantsRepositoryImpl(authenticationManager, api)
    }

    @Provides
    @ActivityScoped
    fun provideMeasurementsRepository(
        authenticationManager: IAuthenticationManager,
        api: HousePlantMeasurementsApi
    ): IMeasurementsRepository {
        return MeasurementsRepositoryImpl(authenticationManager, api)
    }

    @Provides
    @ActivityScoped
    fun provideDevicesRepository(
        authenticationManager: IAuthenticationManager,
        api: HousePlantMeasurementsApi
    ): IDevicesRepository {
        return DevicesRepositoryImpl(authenticationManager, api)
    }
}