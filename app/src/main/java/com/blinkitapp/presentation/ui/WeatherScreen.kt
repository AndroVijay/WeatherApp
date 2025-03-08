package com.blinkitapp.presentation.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.blinkitapp.data.model.WeatherResponse
import com.blinkitapp.presentation.utils.Resource
import com.blinkitapp.presentation.viewmodel.LocationViewmodel
import com.blinkitapp.presentation.viewmodel.WeatherViewModel
import java.time.Instant
import java.time.ZoneId


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel,locationViewmodel: LocationViewmodel) {
    val weatherState by viewModel.weatherState.collectAsState()
    val currentAddress by locationViewmodel.address.collectAsState()
    val city by locationViewmodel.city.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }


    Log.d("MyLocation",city)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.verticalGradient(listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))))) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            LaunchedEffect(currentAddress) {
                viewModel.fetchWeather(currentAddress)
            }
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it
                    locationViewmodel.searchCity(it.text)}, // Update search in real-time
                label = { Text("Search city  here", fontSize = 16.sp, fontStyle = FontStyle.Italic, color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 20.sp, fontStyle = FontStyle.Italic),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White,
                        modifier = Modifier.clickable { viewModel.fetchWeather(city) }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            when (weatherState) {
                is Resource.Loading -> CircularProgressIndicator()
                is Resource.Success -> {AnimatedWeatherCard(weatherState)}
                is Resource.Error -> Text("Error: ${(weatherState as Resource.Error).message}", color = Color.Red)
            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnimatedWeatherCard(weatherState: Resource<WeatherResponse>) {
    AnimatedVisibility(visible = weatherState is Resource.Success) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            weatherState.data?.let { it ->
                Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                    Text("🌍 City: ${it.name}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🌡️ Temperature: ${it.main.temp}°C", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("💧 Humidity: ${it.main.humidity}%", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🌬️ Wind Speed: ${it.wind.speed} m/s", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🧭 Pressure: ${it.main.pressure} hPa", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("☀️ Sunrise: ${Instant.ofEpochSecond(it.sys.sunrise).atZone(
                        ZoneId.systemDefault()).toLocalTime()}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🌇 Sunset: ${Instant.ofEpochSecond(it.sys.sunset).atZone(ZoneId.systemDefault()).toLocalTime()}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}


