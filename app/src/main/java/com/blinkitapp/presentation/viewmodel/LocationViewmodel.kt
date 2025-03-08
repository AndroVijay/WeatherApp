package com.blinkitapp.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blinkitapp.presentation.utils.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewmodel @Inject constructor(private val locationService: LocationService): ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    private val _address = MutableStateFlow<String>("New Delhi")
    var address = _address.asStateFlow()

    private val _city = MutableStateFlow("Enter a city name")
    val city = _city.asStateFlow()

    // Start real-time location updates
    fun startLocationUpdates() {
        viewModelScope.launch {
            locationService.getLocationUpdates().collect { loc ->
                _location.value = loc
                updateAddress(loc.latitude, loc.longitude)
            }
        }
    }

    // Update address from latitude and longitude
    private fun updateAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _address.value = locationService.getAddressFromLocation(latitude, longitude)
        }
    }


    // Search city and update coordinates
    fun searchCity(cityName: String) {
        viewModelScope.launch {
            locationService.getCoordinatesFromCity(cityName)?.let { (lat, lon) ->
                _city.value = locationService.getAddressFromLocation(lat, lon)
            } ?: run {
                _city.value = "City not found"
            }
        }
    }





}