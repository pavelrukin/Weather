package com.pavelrukin.weather.utils.extensions

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.pavelrukin.weather.R
import com.pavelrukin.weather.utils.Constants
import com.squareup.picasso.Picasso

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })


}


fun getWindDestination(windDestination: Int, imageView: AppCompatImageView) {
    when (windDestination) {
        0, 360 -> Picasso.get().load(R.drawable.ic_icon_wind_n)
            .placeholder(R.drawable.ic_icon_wind_n)
            .into(imageView)
        in 1..89 -> Picasso.get().load(R.drawable.ic_icon_wind_ne)
            .placeholder(R.drawable.ic_icon_wind_ne)
            .into(imageView)
        90 -> Picasso.get().load(R.drawable.ic_icon_wind_e)
            .placeholder(R.drawable.ic_icon_wind_e)
            .into(imageView)
        in 91..179 -> Picasso.get().load(R.drawable.ic_icon_wind_se)
            .placeholder(R.drawable.ic_icon_wind_se)
            .into(imageView)
        180 -> Picasso.get().load(R.drawable.ic_icon_wind_s)
            .placeholder(R.drawable.ic_icon_wind_s)
            .into(imageView)
        in 181..269 -> Picasso.get().load(R.drawable.ic_icon_wind_ws)
            .placeholder(R.drawable.ic_icon_wind_ws)
            .into(imageView)
        270 -> Picasso.get().load(R.drawable.ic_icon_wind_w)
            .placeholder(R.drawable.ic_icon_wind_w)
            .into(imageView)
        in 271..359 -> Picasso.get().load(R.drawable.ic_icon_wind_wn)
            .placeholder(R.drawable.ic_icon_wind_wn)
            .into(imageView)
    }
}

fun getWeatherIconWhite(
    weatherId: List<Int>,
    iconString: String,
    imageView: AppCompatImageView,
    context: Context
) {
    weatherId.forEach { weatherId ->
        when (weatherId) {
            200, 201, 202, 210, 211, 212, 221, 230, 231, 232 ->
                if (iconString == "11d") imageView.setImageDrawable(ContextCompat.getDrawable(context,  R.drawable.ic_white_thunder))
            300, 301, 302, 310, 311, 312, 313, 314, 321 ->
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_day_shower))

            500, 501, 502, 503, 504, 520, 521, 522, 531 ->
                if (iconString == "10d") {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_day_rain))
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_night_rain))
                }
            511, 600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 ->
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_weather_snow))
            701, 711, 721, 731, 741, 751, 761, 762, 771, 781 ->
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_weather_fog))
            800 ->
                if (iconString == "01d") {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_day_bright))
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_night_bright))
                }
            801 -> if (iconString == "02d") {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_day_cloudy))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_night_cloudy))
            }
            802, 803, 804 ->
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_white_weather_cloud))

            else -> Picasso.get().load(Constants.WEATHER_ICON + iconString + Constants.PNG)
                .into(imageView)
        }
    }
}

fun getWeatherIconBlack(
    weatherId: List<Int>,
    iconString: String,
    imageView: AppCompatImageView,
    context: Context
) {
    weatherId.forEach { weatherId ->
        when (weatherId) {
            200, 201, 202, 210, 211, 212, 221, 230, 231, 232 ->
                if (iconString == "11d") imageView.setImageDrawable(ContextCompat.getDrawable(context,  R.drawable.ic_black_thunder))
            300, 301, 302, 310, 311, 312, 313, 314, 321 ->
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_day_shower))
            500, 501, 502, 503, 504, 520, 521, 522, 531 ->
                if (iconString == "10d") {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_day_rain))
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_night_rain))
                }
            511, 600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 ->
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_weather_snow))
            701, 711, 721, 731, 741, 751, 761, 762, 771, 781 ->
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_weather_fog))
            800 ->
                if (iconString == "01d") {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_day_bright))
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_night_bright))
                }
            801 -> if (iconString == "02d") {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_day_cloudy))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_night_cloudy))
            }
            802, 803, 804 ->
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_black_weather_cloud))

            else -> Picasso.get().load(Constants.WEATHER_ICON + iconString + Constants.PNG)
                .into(imageView)
        }
    }
}
