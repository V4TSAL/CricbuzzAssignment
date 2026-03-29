package com.example.cricbuzz.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val date: String,
    val temperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double,
    val weatherCondition: String,
    val icon: String
)
