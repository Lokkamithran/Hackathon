package com.example.weatherreporter

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("?appid=2edd7af6fdac2e58fd01f809d7bc106f&units=metric")
    suspend fun getWeather(@Query(value = "lat")lat: String, @Query(value = "lon")lon: String): Response
}