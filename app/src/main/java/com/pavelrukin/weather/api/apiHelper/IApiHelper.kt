package com.pavelrukin.weather.api.apiHelper

import com.pavelrukin.weather.model.current.CurrentResponse
import com.pavelrukin.weather.model.one_call.OneCallResponse
import retrofit2.Response

interface IApiHelper{
    suspend fun getOneCallWeather(lat: Double, lon: Double): Response<OneCallResponse>
    suspend fun getCurrentWeather(cityName: String): Response<CurrentResponse>
}