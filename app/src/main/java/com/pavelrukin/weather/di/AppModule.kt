package com.pavelrukin.weather.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val appModule = module {
    single { androidApplication().resources }
}

val appModules = listOf(appModule, viewModelModule, repositoryModule, apiModule, retrofitModule)
