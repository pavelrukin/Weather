package com.pavelrukin.weather.ui.weather

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pavelrukin.weather.WeatherApp
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.repository.IWeatherRepository
import com.pavelrukin.weather.repository.LocationLiveDataRepository
import com.pavelrukin.weather.utils.Resource
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class WeatherViewModel(
    private val locationLiveDataRepository: LocationLiveDataRepository,
    private val weatherRepository: IWeatherRepository,
    app: WeatherApp
) :
    AndroidViewModel(app) {

    val oneCallWeather: MutableLiveData<Resource<OneCallResponse>> = weatherRepository.oneCallWeather

    fun getOneCallWeather(lat: Double?, lon: Double?) = viewModelScope.launch {
        weatherRepository.getOneCallWeather(lat, lon)
    }

    fun     getLocationData() = locationLiveDataRepository.observeForever {
        getOneCallWeather(it.latitude, it.longitude)
    }


    fun getCityName(latitude: Double, longitude: Double): String {
        var cityName = ""
        val geocoder = Geocoder(getApplication<WeatherApp>(), Locale.getDefault())
        val adress = geocoder.getFromLocation(latitude, longitude, 1)
        try {
            if (adress[0].locality != null) {
                cityName = adress[0].locality
            }
        } catch (e: IOException) {
            Log.d("TAG_ViewModel", "getCity $e")
        }
        return cityName
    }


}