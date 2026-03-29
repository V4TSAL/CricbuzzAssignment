package com.example.cricbuzz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cricbuzz.data.local.ForecastEntity
import com.example.cricbuzz.ui.theme.CricbuzzTheme
import com.example.cricbuzz.util.formatDate

@Composable
fun ForecastCard(
    forecast: ForecastEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatDate(forecast.date),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${forecast.temperature.toInt()}°C",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "H: ${forecast.maxTemperature.toInt()}°  L: ${forecast.minTemperature.toInt()}°",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = forecast.weatherCondition,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            AsyncImage(
                model = "https://openweathermap.org/img/wn/${forecast.icon}@2x.png",
                contentDescription = forecast.weatherCondition,
                modifier = Modifier.size(72.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForecastCardPreview() {
    CricbuzzTheme {
        ForecastCard(
            forecast = ForecastEntity(
                id = 1,
                city = "London",
                date = "2026-03-28",
                temperature = 18.0,
                minTemperature = 12.0,
                maxTemperature = 22.0,
                weatherCondition = "Clouds",
                icon = "04d"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForecastCardSunnyPreview() {
    CricbuzzTheme {
        ForecastCard(
            forecast = ForecastEntity(
                id = 2,
                city = "Mumbai",
                date = "2026-03-29",
                temperature = 34.0,
                minTemperature = 28.0,
                maxTemperature = 37.0,
                weatherCondition = "Clear",
                icon = "01d"
            )
        )
    }
}
