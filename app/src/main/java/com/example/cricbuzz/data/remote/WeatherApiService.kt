package com.example.cricbuzz.data.remote

import com.example.cricbuzz.data.remote.dto.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int = 24
    ): ForecastResponse

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
        const val API_KEY = "428e20ababb360decef988fa774641bd"
    }
}
