package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.IMeasurementsValidator
import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.MeasurementsValidatorImpl
import cz.mendelu.xmusil5.plantmonitor.utils.validation.strings.IStringValidator
import cz.mendelu.xmusil5.plantmonitor.utils.validation.strings.StringValidatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilitiesModule {

    @Provides
    @Singleton
    fun provideStringValidator(): IStringValidator {
        return StringValidatorImpl()
    }

    @Provides
    @Singleton
    fun provideMeasurementsValidator(): IMeasurementsValidator{
        return MeasurementsValidatorImpl()
    }
}