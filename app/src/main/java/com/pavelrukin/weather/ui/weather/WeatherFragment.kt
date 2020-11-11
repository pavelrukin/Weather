package com.pavelrukin.weather.ui.weather


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.pavelrukin.weather.R
import com.pavelrukin.weather.databinding.WeatherFragmentBinding
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.ui.adapter.DailyAdapter
import com.pavelrukin.weather.ui.adapter.HourlyAdapter
import com.pavelrukin.weather.utils.Constants
import com.pavelrukin.weather.utils.Constants.Companion.AUTOCOMPLETE_REQUEST_CODE
import com.pavelrukin.weather.utils.Constants.Companion.GPS_REQUEST
import com.pavelrukin.weather.utils.Constants.Companion.LOCATION_REQUEST
import com.pavelrukin.weather.utils.Constants.Companion.PNG
import com.pavelrukin.weather.utils.Constants.Companion.WEATHER_ICON
import com.pavelrukin.weather.utils.GpsUtils
import com.pavelrukin.weather.utils.Resource
import com.pavelrukin.weather.utils.extensions.dateFormat
import com.pavelrukin.weather.utils.extensions.deleteBrackets
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt


class WeatherFragment : Fragment() {
    val TAG = "WEATHER_FRAGMENT_TAG"
    val args: WeatherFragmentArgs by navArgs()

    private lateinit var binding: WeatherFragmentBinding
    private val viewModel: WeatherViewModel by viewModel()
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter

    private var isGPSEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.weather_fragment, container, false)
        return binding.root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.weather_menu_search_city -> searchView()
            R.id.weather_menu_your_current_location -> invokeLocationAction()
        }
        return super.onOptionsItemSelected(item)
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
        //   placesClient = Places.createClient(activity?.applicationContext!!)


        fetchWeatherFromMap()


        GpsUtils(activity?.applicationContext!!).turnGPSOn(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                this@WeatherFragment.isGPSEnabled = isGPSEnable
            }
        })
    }

    fun fetchWeatherFromMap() {
        if (args.longitude != null && args.latitude != null) {

            val latitude = args.latitude?.toDouble()
            val longitude = args.longitude?.toDouble()
            viewModel.getOneCallWeather(lat = latitude, lon = longitude)
        }


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
        viewModel.oneCallWeather.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { result ->
                        showView()
                        hideProgressBar()
                        bindingView(result)

                    }

                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        hideView()
                        Toast.makeText(activity, "An error: $message", Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "Message error === $message")
                    }
                }
                is Resource.Loading -> {
                    hideView()
                    showProgressBar()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun bindingView(result: OneCallResponse) {
        binding.tvCityName.text = viewModel.getCityName(result.lat, result.lon)

        binding.tvMinTemp.text = result.daily[0].temp.min.roundToInt()
            .toString() + getString(R.string.celsius)
        binding.tvHumidity.text =
            result.current.humidity.toString() + getString(R.string.percent)
        binding.tvMaxTemp.text = result.daily[0].temp.max.roundToInt()
            .toString() + getString(R.string.celsius) + " / "
        binding.tvDate.text = dateFormat(result.current.dt).toString()
        binding.tvWindText.text = result.current.windSpeed.roundToInt()
            .toString() + " " + getString(R.string.wind_text)
        //recycler views
        dailyAdapter.differ.submitList(result.daily)
        hourlyAdapter.differ.submitList(result.hourly)

        //wind destination
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

        //weather icon
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


    fun searchView() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(activity?.applicationContext!!)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private fun invokeLocationAction() {

        when {
            !isGPSEnabled -> Toast.makeText(
                activity,
                getString(R.string.enable_gps),
                Toast.LENGTH_SHORT
            ).show()
            isPermissionsGranted() -> startLocationUpdate()
            shouldShowRequestPermissionRationale() -> Toast.makeText(
                activity,
                getString(R.string.permission_request),
                Toast.LENGTH_SHORT
            ).show()
            else -> ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        viewModel.getLocationData().observe(viewLifecycleOwner, {
            viewModel.getOneCallWeather(it.latitude, it.longitude)
        })
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AUTOCOMPLETE_REQUEST_CODE ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            val place = Autocomplete.getPlaceFromIntent(data)
                            viewModel.getOneCallWeather(
                                lat = place.latLng?.latitude,
                                lon = place.latLng?.longitude
                            )
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        //  Handle the error.
                        data?.let {
                            val status = Autocomplete.getStatusFromIntent(data)
                            Log.i(TAG, status.statusMessage!!)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        // The user canceled the operation.
                    }
                }
            else -> return
            //NextRequestCode
        }
        when (resultCode) {
            Activity.RESULT_OK ->
                if (requestCode == GPS_REQUEST) {
                    isGPSEnabled = true
                    invokeLocationAction()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideView() {
        binding.apply {
            ivIconTemp.visibility = View.INVISIBLE
            ivIconHumidity.visibility = View.INVISIBLE
            ivIconWind.visibility = View.INVISIBLE
            ivIconWindDestination.visibility = View.INVISIBLE
            rvDaily.visibility = View.INVISIBLE
            rvHourly.visibility = View.INVISIBLE
            ivIconWeather.visibility = View.INVISIBLE
            tvCityName.visibility = View.INVISIBLE
            tvDate.visibility = View.INVISIBLE
            tvHumidity.visibility = View.INVISIBLE
            tvMaxTemp.visibility = View.INVISIBLE
            tvMinTemp.visibility = View.INVISIBLE
            tvWindText.visibility = View.INVISIBLE
        }
    }

    fun showView() {
        binding.apply {
            ivIconTemp.visibility = View.VISIBLE
            ivIconHumidity.visibility = View.VISIBLE
            ivIconWind.visibility = View.VISIBLE
            ivIconTemp.visibility = View.VISIBLE
            ivIconHumidity.visibility = View.VISIBLE
            ivIconWindDestination.visibility = View.VISIBLE
            rvDaily.visibility = View.VISIBLE
            rvHourly.visibility = View.VISIBLE
            ivIconWeather.visibility = View.VISIBLE
            tvCityName.visibility = View.VISIBLE
            tvDate.visibility = View.VISIBLE
            tvHumidity.visibility = View.VISIBLE
            tvMaxTemp.visibility = View.VISIBLE
            tvMinTemp.visibility = View.VISIBLE
            tvWindText.visibility = View.VISIBLE
        }
    }


}


