package com.example.cricbuzz.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cricbuzz.R
import com.example.cricbuzz.data.local.ForecastEntity
import com.example.cricbuzz.ui.components.ForecastCard
import com.example.cricbuzz.ui.components.SearchBar
import com.example.cricbuzz.ui.theme.CricbuzzTheme
import com.example.cricbuzz.viewmodel.WeatherUiState
import com.example.cricbuzz.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    WeatherScreenContent(
        uiState = uiState,
        onSearch = { viewModel.fetchForecast(it) },
        onRefresh = { viewModel.refreshForecast() },
        modifier = modifier
    )
}

@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(onSearch = onSearch)

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            uiState.forecasts.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Search for a city to see the 3-day forecast",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                if (uiState.cityName.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = uiState.cityName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            if (uiState.isOfflineData) {
                                Text(
                                    text = "Showing cached data",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(onClick = onRefresh) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_refresh),
                                contentDescription = "Refresh",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyColumn {
                    items(uiState.forecasts) { forecast ->
                        ForecastCard(forecast = forecast)
                    }
                }
            }
        }
    }
}

private val sampleForecasts = listOf(
    ForecastEntity(
        id = 1, city = "London", date = "2026-03-28",
        temperature = 18.0, minTemperature = 12.0, maxTemperature = 22.0,
        weatherCondition = "Clouds", icon = "04d"
    ),
    ForecastEntity(
        id = 2, city = "London", date = "2026-03-29",
        temperature = 21.0, minTemperature = 14.0, maxTemperature = 24.0,
        weatherCondition = "Clear", icon = "01d"
    ),
    ForecastEntity(
        id = 3, city = "London", date = "2026-03-30",
        temperature = 16.0, minTemperature = 10.0, maxTemperature = 19.0,
        weatherCondition = "Rain", icon = "10d"
    )
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WeatherScreenEmptyPreview() {
    CricbuzzTheme {
        WeatherScreenContent(
            uiState = WeatherUiState(),
            onSearch = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WeatherScreenLoadingPreview() {
    CricbuzzTheme {
        WeatherScreenContent(
            uiState = WeatherUiState(isLoading = true),
            onSearch = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WeatherScreenWithDataPreview() {
    CricbuzzTheme {
        WeatherScreenContent(
            uiState = WeatherUiState(
                forecasts = sampleForecasts,
                cityName = "London"
            ),
            onSearch = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WeatherScreenOfflinePreview() {
    CricbuzzTheme {
        WeatherScreenContent(
            uiState = WeatherUiState(
                forecasts = sampleForecasts,
                cityName = "London",
                isOfflineData = true
            ),
            onSearch = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WeatherScreenErrorPreview() {
    CricbuzzTheme {
        WeatherScreenContent(
            uiState = WeatherUiState(error = "City not found. Please try again."),
            onSearch = {},
            onRefresh = {}
        )
    }
}
