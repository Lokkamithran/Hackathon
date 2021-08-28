package com.example.weatherreporter

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRetriever {
    private val service: WeatherService
    companion object{
        const val baseURL = "https://api.openweathermap.org/data/2.5/weather/"
    }
    init {
        val retrofit =Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(WeatherService::class.java)
    }
    suspend fun getWeather(lat: String, lon: String): Response{
        return service.getWeather(lat, lon)
    }

}