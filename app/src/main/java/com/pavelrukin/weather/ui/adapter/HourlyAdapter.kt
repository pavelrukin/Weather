

package com.pavelrukin.weather.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelrukin.weather.R
import com.pavelrukin.weather.databinding.HourlyItemBinding
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.utils.extensions.dateFormatHourly
import com.pavelrukin.weather.utils.extensions.deleteBrackets
import com.pavelrukin.weather.utils.extensions.getWeatherIconWhite
import kotlin.math.roundToInt


class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {
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
    override fun getItemCount() : Int {
        return   differ.currentList.size-12
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
            getWeatherIconWhite(
                weatherIdPath,
                iconString,
                binding.ivIconWeatherHourly,
                itemView.context
            )
            binding.tvHourlyTemp.text = daily.temp.roundToInt().toString()+ itemView.context.getString(
                R.string.celsius
            )
            binding.tvHourlyTime.text = dateFormatHourly(daily.dt)
            binding.executePendingBindings()
        }
    }
}

