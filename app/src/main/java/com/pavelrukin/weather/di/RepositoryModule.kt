package com.pavelrukin.weather.di


import com.pavelrukin.weather.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        WeatherRepository(get())
    }
}
