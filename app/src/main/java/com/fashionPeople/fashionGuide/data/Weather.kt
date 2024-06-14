package com.fashionPeople.fashionGuide.data

data class Weather(
    val maxTemp: Double,
    val minTemp: Double,
    val currentTemp: Double,
    val humidity: Double,
    val weather: String,
    val windSpeed: Double)
