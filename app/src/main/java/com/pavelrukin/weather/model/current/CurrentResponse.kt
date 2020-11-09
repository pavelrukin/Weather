package com.pavelrukin.weather.model.current


import com.google.gson.annotations.SerializedName

data class CurrentResponse(
    @SerializedName("coord")
    var coord: Coord,
    @SerializedName("weather")
    var weather: List<Weather>,
    @SerializedName("base")
    var base: String, // stations
    @SerializedName("main")
    var main: Main,
    @SerializedName("visibility")
    var visibility: Int, // 10000
    @SerializedName("wind")
    var wind: Wind,
    @SerializedName("clouds")
    var clouds: Clouds,
    @SerializedName("dt")
    var dt: Long, // 1604516502
    @SerializedName("sys")
    var sys: Sys,
    @SerializedName("timezone")
    var timezone: Int, // 0
    @SerializedName("id")
    var id: Int, // 2643743
    @SerializedName("name")
    var name: String, // London
    @SerializedName("cod")
    var cod: Int // 200
) {
    data class Coord(
        @SerializedName("lon")
        var lon: Double, // -0.13
        @SerializedName("lat")
        var lat: Double // 51.51
    )

    data class Weather(
        @SerializedName("id")
        var id: Int, // 802
        @SerializedName("main")
        var main: String, // Clouds
        @SerializedName("description")
        var description: String, // scattered clouds
        @SerializedName("icon")
        var icon: String // 03n
    )

    data class Main(
        @SerializedName("temp")
        var temp: Double, // 7.41
        @SerializedName("feels_like")
        var feelsLike: Double, // 5.12
        @SerializedName("temp_min")
        var tempMin: Double, // 5
        @SerializedName("temp_max")
        var tempMax: Double, // 10
        @SerializedName("pressure")
        var pressure: Int, // 1036
        @SerializedName("humidity")
        var humidity: Int // 71
    )

    data class Wind(
        @SerializedName("speed")
        var speed: Double, // 1
        @SerializedName("deg")
        var deg: Int // 290
    )

    data class Clouds(
        @SerializedName("all")
        var all: Int // 31
    )

    data class Sys(
        @SerializedName("type")
        var type: Int, // 1
        @SerializedName("id")
        var id: Int, // 1414
        @SerializedName("country")
        var country: String, // GB
        @SerializedName("sunrise")
        var sunrise: Int, // 1604473194
        @SerializedName("sunset")
        var sunset: Int // 1604507295
    )
}