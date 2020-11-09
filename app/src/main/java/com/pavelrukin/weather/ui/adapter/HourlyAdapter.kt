package com.pavelrukin.weather.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelrukin.weather.R
import com.pavelrukin.weather.databinding.HourlyItemBinding
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.utils.Constants
import com.pavelrukin.weather.utils.extensions.dateFormatHourly
import com.pavelrukin.weather.utils.extensions.deleteBrackets
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt


class HourlyAdapter() :
    RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {


    private val diffUtilCallBack = object : DiffUtil.ItemCallback<OneCallResponse.Hourly>() {
        override fun areItemsTheSame(
            oldItem: OneCallResponse.Hourly,
            newItem: OneCallResponse.Hourly
        ): Boolean {
            return oldItem.weather.map { it.id } == newItem.weather.map { it.id } &&
                    oldItem.temp == newItem.temp &&
                    oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(
            oldItem: OneCallResponse.Hourly,
            newItem: OneCallResponse.Hourly
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding: HourlyItemBinding = DataBindingUtil.inflate(
            inflater,

            R.layout.hourly_item, parent, false
        )

        return HourlyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size -24
    }


    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {

        val hourly = differ.currentList[position]
        holder.bind(hourly)


    }


    inner class HourlyViewHolder(val binding: HourlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(daily: OneCallResponse.Hourly) {
            val weatherIdPath = differ.currentList.flatMap { it.weather.map { it.id } }
            val iconString = differ.currentList.map { it.weather.map { it.icon } }.toString().deleteBrackets()
            val iconPath = Constants.WEATHER_ICON + iconString + Constants.PNG

            binding.ivIconWeatherHourly.apply {
                weatherIdPath.forEach { weatherId ->

                    when (weatherId) {
                        200, 201, 202, 210, 211, 212, 221, 230, 231, 232 ->

                            if (iconString == "11d")
                                binding.ivIconWeatherHourly.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_white_thunder
                                    )
                                )

                        300, 301, 302, 310, 311, 312, 313, 314, 321 ->
                            binding.ivIconWeatherHourly.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_white_day_shower
                                )
                            )
                        500, 501, 502, 503, 504, 520, 521, 522, 531 ->
                            if (iconPath == "10d") {
                                binding.ivIconWeatherHourly.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_white_day_rain
                                    )
                                )
                            } else {
                                binding.ivIconWeatherHourly.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_white_night_rain
                                    )
                                )

                            }

                        511, 600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> binding.ivIconWeatherHourly.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_white_weather_snow
                            )
                        )

                        701, 711, 721, 731, 741, 751, 761, 762, 771, 781 -> binding.ivIconWeatherHourly.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_white_weather_fog
                            )
                        )

                        800 ->
                            if (iconPath == "01d") {
                                binding.ivIconWeatherHourly.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_white_day_bright
                                    )
                                )
                            } else {
                                binding.ivIconWeatherHourly.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_white_night_bright
                                    )
                                )
                            }
                        801 -> if (iconPath == "02d") {
                            binding.ivIconWeatherHourly.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_white_day_cloudy
                                )
                            )
                        } else {
                            binding.ivIconWeatherHourly.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_white_night_cloudy
                                )
                            )
                        }
                        802, 803, 804 -> binding.ivIconWeatherHourly.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_white_weather_cloud
                            )
                        )
                        else -> Picasso.get().load(iconPath)
                            .into(binding.ivIconWeatherHourly)

                    }
                }
            }
            binding.tvHourlyTemp.text = daily.temp.roundToInt().toString()+ itemView.context.getString(
                R.string.celsius)
            binding.tvHourlyTime.text = dateFormatHourly(daily.dt)

            binding.executePendingBindings()
        }
    }

}