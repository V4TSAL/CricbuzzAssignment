package com.example.cricbuzz.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: CityInfo
)

data class CityInfo(
    val name: String
)

data class ForecastItem(
    val dt: Long,
    @SerializedName("dt_txt") val dtTxt: String,
    val main: MainInfo,
    val weather: List<WeatherInfo>
)

data class MainInfo(
    val temp: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double
)

data class WeatherInfo(
    val main: String,
    val description: String,
    val icon: String
)
