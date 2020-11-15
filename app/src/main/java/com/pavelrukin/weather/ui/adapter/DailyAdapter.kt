package com.pavelrukin.weather.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelrukin.weather.R
import com.pavelrukin.weather.databinding.DailyItemBinding
import com.pavelrukin.weather.model.one_call.OneCallResponse
import com.pavelrukin.weather.utils.extensions.dateFormatDayWeek
import com.pavelrukin.weather.utils.extensions.deleteBrackets
import com.pavelrukin.weather.utils.extensions.getWeatherIconBlack
import kotlin.math.roundToInt


class DailyAdapter() :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {


    private val diffUtilCallBack = object : DiffUtil.ItemCallback<OneCallResponse.Daily>() {
        override fun areItemsTheSame(
            oldItem: OneCallResponse.Daily,
            newItem: OneCallResponse.Daily
        ): Boolean {
            return oldItem.temp.max == newItem.temp.max &&
                    oldItem.temp.min == newItem.temp.min &&
                    oldItem.dt == newItem.dt &&
                    oldItem.weather.map { it.id } == newItem.weather.map { it.id }
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
        return differ.currentList.size - 1
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
            val iconString =
                differ.currentList.map { it.weather.map { it.icon } }.toString().deleteBrackets()
            getWeatherIconBlack(weatherIdPath, iconString, binding.ivDailyItemWeather, context = itemView.context)
            binding.tvDailyItemDate.text = dateFormatDayWeek(daily.dt)
            binding.tvDailyItemMaxTemp.text =
                daily.temp.max.roundToInt().toString() + itemView.context.getString(
                    R.string.celsius
                )
            binding.tvDailyItemMinTemp.text =
                daily.temp.min.roundToInt().toString() + itemView.context.getString(
                    R.string.celsius
                )
            binding.executePendingBindings()

        }
    }
}