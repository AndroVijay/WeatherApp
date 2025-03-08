package com.blinkitapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blinkitapp.data.model.WeatherResponse
import com.blinkitapp.domain.usecase.GetWeatherUseCase
import com.blinkitapp.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<Resource<WeatherResponse>>(Resource.Loading())
    val weatherState: StateFlow<Resource<WeatherResponse>> = _weatherState

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _weatherState.value = Resource.Loading()
            try {
                val response = getWeatherUseCase(city)
                _weatherState.value = Resource.Success(response)
            } catch (e: Exception) {
                _weatherState.value = Resource.Error("Unable to find weather report", null)
            }
        }
    }
}