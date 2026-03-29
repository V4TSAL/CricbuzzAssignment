package com.example.cricbuzz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Query("SELECT * FROM forecast WHERE city = :city ORDER BY date ASC")
    suspend fun getForecastByCity(city: String): List<ForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(forecasts: List<ForecastEntity>)

    @Query("DELETE FROM forecast WHERE city = :city")
    suspend fun deleteForecastByCity(city: String)

    @Query("SELECT * FROM forecast ORDER BY id DESC LIMIT 3")
    suspend fun getLastCachedForecast(): List<ForecastEntity>
}
