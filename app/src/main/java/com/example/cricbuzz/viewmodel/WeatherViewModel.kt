package com.example.cricbuzz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cricbuzz.data.local.ForecastEntity
import com.example.cricbuzz.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeatherUiState(
    val forecasts: List<ForecastEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val cityName: String = "",
    val isOfflineData: Boolean = false
)

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        loadCachedForecast()
    }

    private fun loadCachedForecast() {
        viewModelScope.launch {
            val cached = repository.getLastCachedForecast()
            if (cached.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(
                    forecasts = cached,
                    cityName = cached.first().city,
                    isOfflineData = true
                )
            }
        }
    }

    fun fetchForecast(city: String) {
        if (city.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = repository.getForecast(city.trim())

            result.onSuccess { forecasts ->
                _uiState.value = _uiState.value.copy(
                    forecasts = forecasts,
                    isLoading = false,
                    cityName = forecasts.firstOrNull()?.city ?: city,
                    isOfflineData = false
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Failed to fetch weather data"
                )
            }
        }
    }

    fun refreshForecast() {
        val currentCity = _uiState.value.cityName
        if (currentCity.isNotEmpty()) {
            fetchForecast(currentCity)
        }
    }
}
