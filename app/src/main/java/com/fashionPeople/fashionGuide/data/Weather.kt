package com.fashionPeople.fashionGuide.data

data class Weather(var region: String, val maxTemp: Double, val minTemp: Double, val currentTemp: Double, val humidity: Int, val weather: String)
