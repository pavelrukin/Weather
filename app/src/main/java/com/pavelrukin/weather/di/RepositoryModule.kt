package com.pavelrukin.weather.di


import com.pavelrukin.weather.WeatherApp
import com.pavelrukin.weather.api.apiHelper.ApiHelper
import com.pavelrukin.weather.api.apiHelper.IApiHelper
import com.pavelrukin.weather.repository.IWeatherRepository

import com.pavelrukin.weather.repository.LocationLiveDataRepository
import com.pavelrukin.weather.repository.WeatherRepository
import com.pavelrukin.weather.utils.GpsUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<IWeatherRepository> {
        WeatherRepository(app = androidContext()as WeatherApp ,get())
    }
    single { LocationLiveDataRepository(app = androidContext() as WeatherApp) }
      single { GpsUtils(androidContext()) }
    single <IApiHelper>{ ApiHelper(get() )   }

}
