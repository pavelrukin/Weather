package com.pavelrukin.weather.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pavelrukin.weather.WeatherApp
import com.pavelrukin.weather.api.ApiHelper
import com.pavelrukin.weather.api.WeatherApi
import com.pavelrukin.weather.model.current.CurrentResponse
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.utils.Resource
import com.pavelrukin.weather.utils.extensions.isConnected
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class WeatherRepository(private val app: WeatherApp,private val api: ApiHelper)   {

    val oneCallWeather: MutableLiveData<Resource<OneCallResponse>> = MutableLiveData()
    var oneCallWeatherResponse: OneCallResponse? = null

    suspend fun getOneCallWeather(lat: Double?, lon: Double?)  {
        oneCallWeather.postValue(Resource.Loading())
        try {
            if (app.applicationContext.isConnected) {
                oneCallWeather.postValue(Resource.Loading())
                val response = api.getOneCallWeather(lat!!, lon!!)
                oneCallWeather.postValue(handledOneCallWeatherHourly(response))
            } else {
                oneCallWeather.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> oneCallWeather.postValue(Resource.Error("Network Failure"))
                //   is Exception -> oneCallWeather.postValue(Resource.Error("Exception ${t.localizedMessage}"))
                else ->  oneCallWeather.postValue(Resource.Error("Conversion Error ${t.localizedMessage}"))
            }
        }
    }

    private fun handledOneCallWeatherHourly(response: Response<OneCallResponse>): Resource<OneCallResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (oneCallWeatherResponse == null) {
                    oneCallWeatherResponse = resultResponse
                } else {
                    val oldHourly = oneCallWeatherResponse?.hourly
                    val newHourly = resultResponse.hourly
                    val oldDaily = oneCallWeatherResponse?.daily
                    val newDaily = resultResponse.daily
                    oldHourly?.addAll(newHourly)
                    oldDaily?.addAll(newDaily)
                }
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



}
