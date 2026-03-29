package com.example.cricbuzz.data.repository

import com.example.cricbuzz.data.local.ForecastEntity
import com.example.cricbuzz.data.local.WeatherDao
import com.example.cricbuzz.data.remote.WeatherApiService

class WeatherRepository(
    private val apiService: WeatherApiService,
    private val weatherDao: WeatherDao
) {

    suspend fun getForecast(city: String): Result<List<ForecastEntity>> {
        return try {
            val response = apiService.getForecast(
                city = city,
                apiKey = WeatherApiService.API_KEY
            )

            val dailyForecasts = response.list
                .groupBy { it.dtTxt.substring(0, 10) }
                .entries
                .take(3)
                .map { (date, items) ->
                    val representative = items.find { it.dtTxt.contains("12:00:00") } ?: items.first()
                    ForecastEntity(
                        city = response.city.name,
                        date = date,
                        temperature = representative.main.temp,
                        minTemperature = items.minOf { it.main.tempMin },
                        maxTemperature = items.maxOf { it.main.tempMax },
                        weatherCondition = representative.weather.firstOrNull()?.main ?: "Unknown",
                        icon = representative.weather.firstOrNull()?.icon ?: "01d"
                    )
                }

            weatherDao.deleteForecastByCity(response.city.name)
            weatherDao.insertAll(dailyForecasts)

            Result.success(dailyForecasts)
        } catch (e: Exception) {
            val cached = weatherDao.getForecastByCity(city)
            if (cached.isNotEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getLastCachedForecast(): List<ForecastEntity> {
        return weatherDao.getLastCachedForecast()
    }
}
