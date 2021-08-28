package com.example.weatherreporter

data class Response(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val name: String?
)
data class Coord(val lon: Double?,
                 val lat: Double?
)
data class Weather(
    val description: String?,
    val icon: String?
)
data class Main(
    val temp: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val humidity: Int?
)
data class Wind(
    val speed: Double?
)
