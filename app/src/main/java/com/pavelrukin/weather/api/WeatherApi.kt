package com.pavelrukin.weather.api

import com.pavelrukin.weather.model.current.CurrentResponse
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q")
        cityName: String,
        @Query("appid")
        key: String = Constants.API_KEY,
        @Query("units")
        units: String = Constants.UNITS

    ): Response<CurrentResponse>

    @GET("data/2.5/onecall")
    suspend fun getOneCallWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("appid")
        key: String = Constants.API_KEY,
     /*   @Query("cnt")
        cnt: Int = 5,*/
        @Query("units")
        units: String = Constants.UNITS
    ):Response<OneCallResponse>


}
