package com.pavelrukin.weather.utils.extensions
fun String.deleteBrackets() = replace("[\\[\\](){}]".toRegex(), "")