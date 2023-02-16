package cz.mendelu.xmusil5.plantmonitor.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.mendelu.xmusil5.plantmonitor.communication.CommunicationConstants
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        val dispatcher = Dispatcher()

        httpClient.dispatcher(dispatcher)
        httpClient.readTimeout(CommunicationConstants.READ_TIMEOUT, TimeUnit.SECONDS)
        httpClient.connectTimeout(CommunicationConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        return httpClient.addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder().baseUrl(CommunicationConstants.HOUSE_PLANT_MEASUREMENTS_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }



    @Provides
    @Singleton
    fun provideHousePlantsMeasurementsApi(retrofit: Retrofit): HousePlantMeasurementsApi{
        return retrofit.create(HousePlantMeasurementsApi::class.java)
    }
}