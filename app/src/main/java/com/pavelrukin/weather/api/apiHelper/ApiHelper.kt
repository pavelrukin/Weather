package com.pavelrukin.weather.api.apiHelper

import com.pavelrukin.weather.api.WeatherApi
import com.pavelrukin.weather.model.current.CurrentResponse
import com.pavelrukin.weather.model.one_call.OneCallResponse
import retrofit2.Response

class ApiHelper(private val api: WeatherApi) : IApiHelper {
    override suspend fun getCurrentWeather(cityName: String): Response<CurrentResponse> =
        api.getCurrentWeather(cityName = cityName)

    override suspend fun getOneCallWeather(lat: Double, lon: Double): Response<OneCallResponse> =
        api.getOneCallWeather(lat = lat, lon = lon)
}
