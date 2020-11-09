package com.pavelrukin.weather.di


import com.pavelrukin.weather.WeatherApp
import com.pavelrukin.weather.repository.IWeatherRepository
import com.pavelrukin.weather.ui.weather.WeatherViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        WeatherViewModel( get(), app = androidApplication() as WeatherApp)
    }
}