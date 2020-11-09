package com.pavelrukin.weather.repository

import com.pavelrukin.weather.model.current.CurrentResponse
import com.pavelrukin.weather.model.one_call.OneCallResponse
import retrofit2.Response

interface IWeatherRepository {

    suspend fun getCurrentWeather(cityName:String): Response<CurrentResponse>
    suspend fun getOneCallWeather(lat:Double,lon:Double):Response<OneCallResponse>
}
