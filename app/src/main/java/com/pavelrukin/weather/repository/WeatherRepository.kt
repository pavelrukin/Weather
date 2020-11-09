package com.pavelrukin.weather.repository

import com.pavelrukin.weather.api.WeatherApi
import com.pavelrukin.weather.model.current.CurrentResponse
import com.pavelrukin.weather.model.one_call.OneCallResponse
import retrofit2.Response

class WeatherRepository(private val api: WeatherApi) : IWeatherRepository {
    override suspend fun getCurrentWeather(cityName: String): Response<CurrentResponse> =
        api.getCurrentWeather(cityName = cityName)

    override suspend fun getOneCallWeather(lat: Double, lon: Double): Response<OneCallResponse> =
        api.getOneCallWeather(lat = lat, lon = lon)
}
