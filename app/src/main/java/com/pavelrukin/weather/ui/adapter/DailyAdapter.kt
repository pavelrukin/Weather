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
import com.pavelrukin.weather.databinding.DailyItemBinding
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.utils.Constants
import com.pavelrukin.weather.utils.extensions.dateFormatDayWeek
import com.pavelrukin.weather.utils.extensions.deleteBrackets
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt


class  DailyAdapter( ) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {


    private val diffUtilCallBack = object : DiffUtil.ItemCallback<OneCallResponse.Daily>() {
        override fun areItemsTheSame(oldItem: OneCallResponse.Daily, newItem: OneCallResponse.Daily): Boolean {
            return oldItem.temp.max  == newItem.temp.max &&
              oldItem.temp.min  == newItem.temp.min  &&
              oldItem.dt  == newItem.dt &&
              oldItem.weather.map { it.id }  == newItem.weather.map { it.id }
        }

        override fun areContentsTheSame(
            oldItem: OneCallResponse.Daily,
            newItem: OneCallResponse.Daily
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding: DailyItemBinding = DataBindingUtil.inflate(
            inflater,

            R.layout.daily_item, parent, false
        )

        return DailyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size -1
    }


    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {

        val daily = differ.currentList[position]
        holder.bind(daily)


    }


    inner class DailyViewHolder(val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(daily: OneCallResponse.Daily) {
            val weatherIdPath = differ.currentList.flatMap { it.weather.map { it.id } }
            val iconString = differ.currentList.map { it.weather.map { it.icon } }.toString().deleteBrackets()
            val iconPath = Constants.WEATHER_ICON + iconString + Constants.PNG

            binding.ivDailyItemWeather.apply {
                weatherIdPath.forEach { weatherId ->

                    when (weatherId) {
                        200, 201, 202, 210, 211, 212, 221, 230, 231, 232 ->

                            if (iconString == "11d")
                                binding.ivDailyItemWeather.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_black_thunder
                                    )
                                )

                        300, 301, 302, 310, 311, 312, 313, 314, 321 ->
                            binding.ivDailyItemWeather.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_black_day_shower
                                )
                            )
                        500, 501, 502, 503, 504, 520, 521, 522, 531 ->
                            if (iconPath == "10d") {
                                binding.ivDailyItemWeather.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_black_day_rain
                                    )
                                )
                            } else {
                                binding.ivDailyItemWeather.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_black_night_rain
                                    )
                                )

                            }

                        511, 600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> binding.ivDailyItemWeather.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_black_weather_snow
                            )
                        )

                        701, 711, 721, 731, 741, 751, 761, 762, 771, 781 -> binding.ivDailyItemWeather.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_black_weather_fog
                            )
                        )

                        800 ->
                            if (iconPath == "01d") {
                                binding.ivDailyItemWeather.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_black_day_bright
                                    )
                                )
                            } else {
                                binding.ivDailyItemWeather.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_black_night_bright
                                    )
                                )
                            }
                        801 -> if (iconPath == "02d") {
                            binding.ivDailyItemWeather.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_black_day_cloudy
                                )
                            )
                        } else {
                            binding.ivDailyItemWeather.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_black_night_cloudy
                                )
                            )
                        }
                        802, 803, 804 -> binding.ivDailyItemWeather.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_black_weather_cloud
                            )
                        )
                        else -> Picasso.get().load(iconPath)
                            .into(binding.ivDailyItemWeather)

                    }
                }
            }
            binding.tvDailyItemDate.text = dateFormatDayWeek(daily.dt)
            binding.tvDailyItemMaxTemp.text = daily.temp.max.roundToInt().toString() + itemView.context.getString(
                R.string.celsius)
            binding.tvDailyItemMinTemp.text = daily.temp.min.roundToInt().toString() + itemView.context.getString(
                    R.string.celsius)
            binding.executePendingBindings()

        }
    }
}