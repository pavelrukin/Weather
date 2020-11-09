package com.pavelrukin.weather.utils.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun  Any.dateFormat(date:Long) = SimpleDateFormat.getDateInstance().format(Date(date * 1000L))
fun  Any.dateFormatDayWeek(date:Long) = SimpleDateFormat("EEE").format(Date(date * 1000L))
fun  Any.dateFormatHourly(date:Long) = SimpleDateFormat("HH:mm").format(Date(date * 1000L))
