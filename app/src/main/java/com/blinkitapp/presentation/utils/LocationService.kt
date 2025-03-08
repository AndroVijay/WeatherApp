package com.blinkitapp.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(@ApplicationContext private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val geocoder = Geocoder(context, Locale.getDefault())
    // Get real-time location updates using Flow
    @SuppressLint("MissingPermission")
   suspend fun getLocationUpdates(interval: Long = 5000L): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(interval)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // Use PRIORITY_BALANCED_POWER_ACCURACY for battery saving
            .build()

        val locationCallback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                result.lastLocation?.let { trySend(it) }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    // Convert latitude and longitude to address using Geocoder
    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String {

        return withContext(Dispatchers.IO) {
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.locality ?: "City not found"
        } catch (e: IOException) {
            "Error fetching address"
        }
    }
    }


    // Convert a search query (e.g., city name) to latitude and longitude
    suspend fun getCoordinatesFromCity(cityName: String): Pair<Double, Double>? {
        return withContext(Dispatchers.IO) {
            try {
                val addresses: List<Address>? = geocoder.getFromLocationName(cityName, 1)
                addresses?.firstOrNull()?.let { it.latitude to it.longitude }
            } catch (e: IOException) {
                null
            }
        }
    }
}
