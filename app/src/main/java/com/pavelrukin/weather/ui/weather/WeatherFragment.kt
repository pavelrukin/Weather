package com.pavelrukin.weather.ui.weather


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.pavelrukin.weather.R
import com.pavelrukin.weather.databinding.WeatherFragmentBinding
import com.pavelrukin.weather.ui.adapter.DailyAdapter
import com.pavelrukin.weather.ui.adapter.HourlyAdapter
import com.pavelrukin.weather.utils.Constants
import com.pavelrukin.weather.utils.Constants.Companion.AUTOCOMPLETE_REQUEST_CODE
import com.pavelrukin.weather.utils.Constants.Companion.PNG
import com.pavelrukin.weather.utils.Constants.Companion.WEATHER_ICON
import com.pavelrukin.weather.utils.Resource
import com.pavelrukin.weather.utils.extensions.dateFormat
import com.pavelrukin.weather.utils.extensions.deleteBrackets
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt


class WeatherFragment : Fragment() {
    val TAG = "WEATHER_FRAGMENT_TAG"

    private lateinit var binding: WeatherFragmentBinding
    private val viewModel: WeatherViewModel by viewModel()
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.weather_fragment, container, false)
        return binding.root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.weather_menu_search_city -> seacrhView()
            R.id.weather_menu_your_current_location -> {
                Toast.makeText(
                    requireActivity(),
                    "your_current_location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun seacrhView() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(activity?.applicationContext!!)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        fetchOneCallWeather()
        initDailyRV()
        initHourlyRV()
        // Initialize the SDK
        Places.initialize(activity?.applicationContext!!, Constants.GOOGLE_API_KEY)
        // Create a new PlacesClient instance
        val placesClient = Places.createClient(activity?.applicationContext!!)


    }

    private fun initHourlyRV() {
        hourlyAdapter = HourlyAdapter()
        rv_hourly.apply {
            adapter = hourlyAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initDailyRV() {
        dailyAdapter = DailyAdapter()
        rv_daily.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun fetchOneCallWeather() {
        viewModel.oneCallWeather.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { result ->

                        binding.tvMinTemp.text = result.daily[0].temp.min.roundToInt()
                            .toString() + getString(R.string.celsius)
                        binding.tvHumidity.text =
                            result.current.humidity.toString() + getString(R.string.percent)
                        binding.tvMaxTemp.text = result.daily[0].temp.max.roundToInt()
                            .toString() + getString(R.string.celsius) + " / "
                        binding.tvDate.text = dateFormat(result.current.dt).toString()
                        binding.tvWindText.text = result.current.windSpeed.roundToInt()
                            .toString() + " " + getString(R.string.wind_text)


                        dailyAdapter.differ.submitList(result.daily)
                        hourlyAdapter.differ.submitList(result.hourly)
                        val windDestinationPath = result.current.windDeg
                        binding.ivIconWindDestination.apply {
                            when (windDestinationPath) {
                                0, 360 -> Picasso.get().load(R.drawable.ic_icon_wind_n)
                                    .placeholder(R.drawable.ic_icon_wind_n)
                                    .into(binding.ivIconWindDestination)
                                in 1..89 -> Picasso.get().load(R.drawable.ic_icon_wind_ne)
                                    .placeholder(R.drawable.ic_icon_wind_ne)
                                    .into(binding.ivIconWindDestination)
                                 90 -> Picasso.get().load(R.drawable.ic_icon_wind_e)
                                    .placeholder(R.drawable.ic_icon_wind_e)
                                    .into(binding.ivIconWindDestination)
                                in 91..179 -> Picasso.get().load(R.drawable.ic_icon_wind_se)
                                    .placeholder(R.drawable.ic_icon_wind_se)
                                    .into(binding.ivIconWindDestination)
                                 180 -> Picasso.get().load(R.drawable.ic_icon_wind_s)
                                    .placeholder(R.drawable.ic_icon_wind_s)
                                    .into(binding.ivIconWindDestination)
                                in 181..269 -> Picasso.get().load(R.drawable.ic_icon_wind_ws)
                                    .placeholder(R.drawable.ic_icon_wind_ws)
                                    .into(binding.ivIconWindDestination)

                                270 -> Picasso.get().load(R.drawable.ic_icon_wind_w)
                                    .placeholder(R.drawable.ic_icon_wind_w)
                                    .into(binding.ivIconWindDestination)

                                in 271..359 -> Picasso.get().load(R.drawable.ic_icon_wind_wn)
                                    .placeholder(R.drawable.ic_icon_wind_wn)
                                    .into(binding.ivIconWindDestination)


                            }

                        }

                        val weatherIdPath = result.current.weather.map { it.id }
                        val iconString = result.current.weather.map { it.icon }.toString()
                            .deleteBrackets()
                        val iconPath = WEATHER_ICON + iconString + PNG

                        binding.ivIconWeather.apply {
                            weatherIdPath.forEach { weatherId ->

                                when (weatherId) {
                                    200, 201, 202, 210, 211, 212, 221, 230, 231, 232 ->

                                        if (iconString == "11d")
                                            binding.ivIconWeather.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    context,
                                                    R.drawable.ic_white_thunder
                                                )
                                            )

                                    300, 301, 302, 310, 311, 312, 313, 314, 321 ->
                                        binding.ivIconWeather.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                context,
                                                R.drawable.ic_white_day_shower
                                            )
                                        )
                                    500, 501, 502, 503, 504, 520, 521, 522, 531 ->
                                        if (iconPath == "10d") {
                                            binding.ivIconWeather.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    context,
                                                    R.drawable.ic_white_day_rain
                                                )
                                            )
                                        } else {
                                            binding.ivIconWeather.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    context,
                                                    R.drawable.ic_white_night_rain
                                                )
                                            )

                                        }

                                    511, 600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> binding.ivIconWeather.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_white_weather_snow
                                        )
                                    )

                                    701, 711, 721, 731, 741, 751, 761, 762, 771, 781 -> binding.ivIconWeather.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_white_weather_fog
                                        )
                                    )

                                    800 ->
                                        if (iconPath == "01d") {
                                            binding.ivIconWeather.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    context,
                                                    R.drawable.ic_white_day_bright
                                                )
                                            )
                                        } else {
                                            binding.ivIconWeather.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    context,
                                                    R.drawable.ic_white_night_bright
                                                )
                                            )
                                        }
                                    801 -> if (iconPath == "02d") {
                                        binding.ivIconWeather.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                context,
                                                R.drawable.ic_white_day_cloudy
                                            )
                                        )
                                    } else {
                                        binding.ivIconWeather.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                context,
                                                R.drawable.ic_white_night_cloudy
                                            )
                                        )
                                    }
                                    802, 803, 804 -> binding.ivIconWeather.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_white_weather_cloud
                                        )
                                    )
                                    else -> Picasso.get().load(iconPath)
                                        .into(binding.ivIconWeather)

                                }
                            }
                        }

                    }


                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error: $message", Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "Message error === $message")
                    }
                }
                is Resource.Loading -> {
                    //TODO create progressBar
                }
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(
                            TAG,
                            "Place: ${place.name}, ${place.id} " +
                                    "time ${place.addressComponents} " +
                                    "latlng ${place.latLng?.latitude.toString()} ||  ${place.latLng?.longitude.toString()} "
                        )
                        binding.tvCityName.text = place.name
                        viewModel.getOneCallWeatherHourly(
                            lat = place.latLng?.latitude,
                            lon = place.latLng?.longitude

                        )
                        //    viewModel.getCurrentWeather(place.name)
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage!!)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}


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