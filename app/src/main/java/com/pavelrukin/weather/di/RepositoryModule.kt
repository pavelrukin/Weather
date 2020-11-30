package com.pavelrukin.weather.di


import com.pavelrukin.weather.WeatherApp
import com.pavelrukin.weather.api.ApiHelper
import com.pavelrukin.weather.repository.LocationLiveDataRepository
import com.pavelrukin.weather.repository.WeatherRepository
import com.pavelrukin.weather.utils.GpsUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single {
        WeatherRepository(app = androidContext()as WeatherApp ,get())
    }
    single { LocationLiveDataRepository(app = androidContext() as WeatherApp) }
    single { GpsUtils(get()) }
    single { ApiHelper(get()) }
}
