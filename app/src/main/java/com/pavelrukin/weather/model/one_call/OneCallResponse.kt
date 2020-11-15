package com.pavelrukin.weather.model.one_call


import com.google.gson.annotations.SerializedName

data class OneCallResponse(
    @SerializedName("lat")
    var lat: Double, // 33.44
    @SerializedName("lon")
    var lon: Double, // -94.04
    @SerializedName("timezone")
    var timezone: String, // America/Chicago
    @SerializedName("timezone_offset")
    var timezoneOffset: Int, // -21600
    @SerializedName("current")
    var current: Current,
/*    @SerializedName("minutely")
    var minutely: List<Minutely>,*/
    @SerializedName("hourly")
    var hourly: MutableList<Hourly>,
    @SerializedName("daily")
    var daily: MutableList<Daily>
) {
    data class Current(
        @SerializedName("dt")
        var dt: Long, // 1604566256
        @SerializedName("sunrise")
        var sunrise: Int, // 1604579935
        @SerializedName("sunset")
        var sunset: Int, // 1604618437
        @SerializedName("temp")
        var temp: Double, // 11.91
        @SerializedName("feels_like")
        var feelsLike: Double, // 7.52
        @SerializedName("pressure")
        var pressure: Int, // 1024
        @SerializedName("humidity")
        var humidity: Int, // 54
        @SerializedName("dew_point")
        var dewPoint: Double, // 2.91
        @SerializedName("uvi")
        var uvi: Double, // 3.96
        @SerializedName("clouds")
        var clouds: Int, // 1
        @SerializedName("visibility")
        var visibility: Int, // 10000
        @SerializedName("wind_speed")
        var windSpeed: Double, // 4.1
        @SerializedName("wind_deg")
        var windDeg: Int, // 180
        @SerializedName("weather")
        var weather: List<Weather>,

    ) {
        data class Weather(
            @SerializedName("id")
            var id: Int, // 800
            @SerializedName("main")
            var main: String, // Clear
            @SerializedName("description")
            var description: String, // clear sky
            @SerializedName("icon")
            var icon: String // 01n
        )

    }
/*
    data class Minutely(
        @SerializedName("dt")
        var dt: Long, // 1604566260
        @SerializedName("precipitation")
        var precipitation: Int // 0
    )*/

    data class Hourly(
        @SerializedName("dt")
        var dt: Long, // 1604563200
        @SerializedName("temp")
        var temp: Double, // 11.91
        @SerializedName("feels_like")
        var feelsLike: Double, // 8.61
        @SerializedName("pressure")
        var pressure: Int, // 1024
        @SerializedName("humidity")
        var humidity: Int, // 54
        @SerializedName("dew_point")
    var dewPoint: Double, // 2.91
        @SerializedName("clouds")
        var clouds: Int, // 1
        @SerializedName("visibility")
        var visibility: Int, // 10000
        @SerializedName("wind_speed")
        var windSpeed: Double, // 2.54
        @SerializedName("wind_deg")
        var windDeg: Int, // 180
        @SerializedName("weather")
        var weather: List<Weather>,
     @SerializedName("pop")
        var pop: Double, // 0
        @SerializedName("rain")
        var rain: Rain
    ) {
        data class Weather(
            @SerializedName("id")
            var id: Int, // 800
            @SerializedName("main")
            var main: String, // Clear
            @SerializedName("description")
            var description: String, // clear sky
            @SerializedName("icon")
            var icon: String // 01n
        )

        data class Rain(
            @SerializedName("1h")
            var h: Double // 0.15
        )
    }

    data class Daily(
        @SerializedName("dt")
        var dt: Long, // 1604595600
        @SerializedName("sunrise")
        var sunrise: Int, // 1604579935
        @SerializedName("sunset")
        var sunset: Int, // 1604618437
        @SerializedName("temp")
        var temp: Temp,
        @SerializedName("feels_like")
        var feelsLike: FeelsLike,
        @SerializedName("pressure")
        var pressure: Int, // 1024
        @SerializedName("humidity")
        var humidity: Int, // 63
        @SerializedName("dew_point")
        var dewPoint: Double, // 13.52
        @SerializedName("wind_speed")
        var windSpeed: Double, // 2.29
        @SerializedName("wind_deg")
        var windDeg: Int, // 151
        @SerializedName("weather")
        var weather: List<Weather>,
        @SerializedName("clouds")
        var clouds: Int, // 66
        @SerializedName("pop")
        var pop: Double, // 0.37
        @SerializedName("rain")
        var rain: Double, // 1.56
        @SerializedName("uvi")
        var uvi: Double // 3.96
    ) {
        data class Temp(
            @SerializedName("day")
            var day: Double, // 20.82
            @SerializedName("min")
            var min: Double, // 11.25
            @SerializedName("max")
            var max: Double, // 21.67
            @SerializedName("night")
            var night: Double, // 14.61
            @SerializedName("eve")
            var eve: Double, // 16.66
            @SerializedName("morn")
            var morn: Double // 11.25
        )

        data class FeelsLike(
            @SerializedName("day")
            var day: Double, // 20.32
            @SerializedName("night")
            var night: Double, // 13.31
            @SerializedName("eve")
            var eve: Double, // 16.64
            @SerializedName("morn")
            var morn: Double // 8.69
        )

        data class Weather(
            @SerializedName("id")
            var id: Int, // 500
            @SerializedName("main")
            var main: String, // Rain
            @SerializedName("description")
            var description: String, // light rain
            @SerializedName("icon")
            var icon: String // 10d
        )
    }
}