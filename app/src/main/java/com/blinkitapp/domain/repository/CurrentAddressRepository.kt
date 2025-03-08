package com.blinkitapp.domain.repository

import com.blinkitapp.data.model.CurrentAddress

interface CurrentAddressRepository {

    suspend fun getCurrentLocation(latitude: Double, longitude: Double): CurrentAddress
}