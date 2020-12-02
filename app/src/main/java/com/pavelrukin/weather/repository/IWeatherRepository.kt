package com.pavelrukin.weather.repository

import androidx.lifecycle.MutableLiveData
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.utils.Resource
import retrofit2.Response

interface IWeatherRepository {
    val oneCallWeather: MutableLiveData<Resource<OneCallResponse>>
    suspend fun getOneCallWeather(lat: Double?, lon: Double?)
      fun handledOneCallWeatherHourly(response: Response<OneCallResponse>): Resource<OneCallResponse>?
}