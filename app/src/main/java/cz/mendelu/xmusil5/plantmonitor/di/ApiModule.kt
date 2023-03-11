package cz.mendelu.xmusil5.plantmonitor.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.mendelu.xmusil5.plantmonitor.communication.api.ApiConstants
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.jsonAdapters.measurement.MeasurementTypeAdapter
import cz.mendelu.xmusil5.plantmonitor.communication.jsonAdapters.user.RoleAdapter
import cz.mendelu.xmusil5.plantmonitor.communication.jsonAdapters.utils.DateTimeFromApiAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ActivityRetainedComponent::class)
class ApiModule {

    @Provides
    @ActivityRetainedScoped
    fun provideInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @ActivityRetainedScoped
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        val dispatcher = Dispatcher()

        httpClient.dispatcher(dispatcher)
        httpClient.readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.SECONDS)
        httpClient.connectTimeout(ApiConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        return httpClient.addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(RoleAdapter())
            .add(MeasurementTypeAdapter())
            .add(DateTimeFromApiAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder().baseUrl(ApiConstants.HOUSE_PLANT_MEASUREMENTS_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }



    @Provides
    @ActivityRetainedScoped
    fun provideHousePlantsMeasurementsApi(retrofit: Retrofit): HousePlantMeasurementsApi{
        return retrofit.create(HousePlantMeasurementsApi::class.java)
    }
}