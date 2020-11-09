
import com.google.gson.annotations.SerializedName

data class vashington(
    @SerializedName("lat")
    var lat: Double, // 38.91
    @SerializedName("lon")
    var lon: Double, // -77.04
    @SerializedName("timezone")
    var timezone: String, // America/New_York
    @SerializedName("timezone_offset")
    var timezoneOffset: Int, // -18000
    @SerializedName("current")
    var current: Current,
    @SerializedName("minutely")
    var minutely: List<Minutely>,
    @SerializedName("hourly")
    var hourly: List<Hourly>,
    @SerializedName("daily")
    var daily: List<Daily>
) {
    data class Current(
        @SerializedName("dt")
        var dt: Int, // 1604937728
        @SerializedName("sunrise")
        var sunrise: Int, // 1604922288
        @SerializedName("sunset")
        var sunset: Int, // 1604959162
        @SerializedName("temp")
        var temp: Double, // 20.76
        @SerializedName("feels_like")
        var feelsLike: Double, // 20.87
        @SerializedName("pressure")
        var pressure: Int, // 1028
        @SerializedName("humidity")
        var humidity: Int, // 64
        @SerializedName("dew_point")
        var dewPoint: Double, // 13.71
        @SerializedName("uvi")
        var uvi: Double, // 3.2
        @SerializedName("clouds")
        var clouds: Int, // 1
        @SerializedName("visibility")
        var visibility: Int, // 10000
        @SerializedName("wind_speed")
        var windSpeed: Double, // 1.5
        @SerializedName("wind_deg")
        var windDeg: Int, // 220
        @SerializedName("weather")
        var weather: List<Weather>
    ) {
        data class Weather(
            @SerializedName("id")
            var id: Int, // 800
            @SerializedName("main")
            var main: String, // Clear
            @SerializedName("description")
            var description: String, // clear sky
            @SerializedName("icon")
            var icon: String // 01d
        )
    }

    data class Minutely(
        @SerializedName("dt")
        var dt: Int, // 1604937780
        @SerializedName("precipitation")
        var precipitation: Int // 0
    )

    data class Hourly(
        @SerializedName("dt")
        var dt: Int, // 1604937600
        @SerializedName("temp")
        var temp: Double, // 20.76
        @SerializedName("feels_like")
        var feelsLike: Double, // 20.85
        @SerializedName("pressure")
        var pressure: Int, // 1028
        @SerializedName("humidity")
        var humidity: Int, // 64
        @SerializedName("dew_point")
        var dewPoint: Double, // 13.71
        @SerializedName("clouds")
        var clouds: Int, // 1
        @SerializedName("visibility")
        var visibility: Int, // 10000
        @SerializedName("wind_speed")
        var windSpeed: Double, // 1.53
        @SerializedName("wind_deg")
        var windDeg: Int, // 214
        @SerializedName("weather")
        var weather: List<Weather>,
        @SerializedName("pop")
        var pop: Int // 0
    ) {
        data class Weather(
            @SerializedName("id")
            var id: Int, // 800
            @SerializedName("main")
            var main: String, // Clear
            @SerializedName("description")
            var description: String, // clear sky
            @SerializedName("icon")
            var icon: String // 01d
        )
    }

    data class Daily(
        @SerializedName("dt")
        var dt: Int, // 1604937600
        @SerializedName("sunrise")
        var sunrise: Int, // 1604922288
        @SerializedName("sunset")
        var sunset: Int, // 1604959162
        @SerializedName("temp")
        var temp: Temp,
        @SerializedName("feels_like")
        var feelsLike: FeelsLike,
        @SerializedName("pressure")
        var pressure: Int, // 1028
        @SerializedName("humidity")
        var humidity: Int, // 64
        @SerializedName("dew_point")
        var dewPoint: Double, // 13.71
        @SerializedName("wind_speed")
        var windSpeed: Double, // 1.3
        @SerializedName("wind_deg")
        var windDeg: Int, // 208
        @SerializedName("weather")
        var weather: List<Weather>,
        @SerializedName("clouds")
        var clouds: Int, // 1
        @SerializedName("pop")
        var pop: Int, // 0
        @SerializedName("uvi")
        var uvi: Double, // 3.2
        @SerializedName("rain")
        var rain: Double // 22.26
    ) {
        data class Temp(
            @SerializedName("day")
            var day: Double, // 20.76
            @SerializedName("min")
            var min: Double, // 14.46
            @SerializedName("max")
            var max: Double, // 21.94
            @SerializedName("night")
            var night: Double, // 16.41
            @SerializedName("eve")
            var eve: Double, // 21.8
            @SerializedName("morn")
            var morn: Double // 15.12
        )

        data class FeelsLike(
            @SerializedName("day")
            var day: Double, // 21.01
            @SerializedName("night")
            var night: Double, // 15.44
            @SerializedName("eve")
            var eve: Double, // 21.47
            @SerializedName("morn")
            var morn: Double // 15.58
        )

        data class Weather(
            @SerializedName("id")
            var id: Int, // 800
            @SerializedName("main")
            var main: String, // Clear
            @SerializedName("description")
            var description: String, // clear sky
            @SerializedName("icon")
            var icon: String // 01d
        )
    }
}