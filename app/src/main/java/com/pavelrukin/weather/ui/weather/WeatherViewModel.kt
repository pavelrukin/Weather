package com.pavelrukin.weather.ui.weather

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pavelrukin.weather.WeatherApp
import com.pavelrukin.weather.model.current.CurrentResponse
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.repository.WeatherRepository
import com.pavelrukin.weather.utils.Resource
import com.pavelrukin.weather.utils.extensions.isConnected
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class WeatherViewModel(val repository: WeatherRepository, app: WeatherApp) :
    AndroidViewModel(app) {


    val currentWeather: MutableLiveData<Resource<CurrentResponse>> = MutableLiveData()
    var currentWeatherResponse: CurrentResponse? = null

     val oneCallWeather: MutableLiveData<Resource<OneCallResponse>> = MutableLiveData()
    var oneCallWeatherResponse: OneCallResponse? = null

    fun getCurrentWeather(cityName: String?) = viewModelScope.launch {
        currentWeather.postValue(Resource.Loading())
        try {
            if (getApplication<WeatherApp>().isConnected) {
                currentWeather.postValue(Resource.Loading())
                val response = repository.getCurrentWeather(cityName!!)
                currentWeather.postValue(handledCurrentWeather(response))
            } else {
                currentWeather.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> currentWeather.postValue(Resource.Error("Networ Failure"))
                else -> currentWeather.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handledCurrentWeather(response: Response<CurrentResponse>): Resource<CurrentResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (currentWeatherResponse == null) {
                    currentWeatherResponse = resultResponse
                } else {
                    val old = currentWeatherResponse
                    val new = resultResponse

                }
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getOneCallWeather(lat: Double?, lon: Double?) = viewModelScope.launch {
        oneCallWeather.postValue(Resource.Loading())
        try {
            if (getApplication<WeatherApp>().isConnected) {
                oneCallWeather.postValue(Resource.Loading())
                val response = repository.getOneCallWeather(lat!!, lon!!)
                oneCallWeather.postValue(handledOneCallWeatherHourly(response))
            } else {
                oneCallWeather.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> oneCallWeather.postValue(Resource.Error("Networ Failure"))
                else -> oneCallWeather.postValue(Resource.Error("Conversion Error"))
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