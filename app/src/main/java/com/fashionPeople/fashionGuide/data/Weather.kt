package com.fashionPeople.fashionGuide.data

data class Weather(
    val currentTemp: Double,
    val humidity: Double,
    val maxTemp: Double,
    val minTemp: Double,
    val region: String,
    val weather: String,
    val windSpeed: Double)
