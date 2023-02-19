package cz.mendelu.xmusil5.plantmonitor.di

import cz.mendelu.xmusil5.plantmonitor.utils.validation.IStringValidator
import cz.mendelu.xmusil5.plantmonitor.utils.validation.StringValidatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Provides
    @Singleton
    fun provideStringValidator(): IStringValidator {
        return StringValidatorImpl()
    }
}