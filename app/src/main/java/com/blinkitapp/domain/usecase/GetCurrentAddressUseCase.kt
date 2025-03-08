package com.blinkitapp.domain.usecase

import com.blinkitapp.domain.repository.CurrentAddressRepository
import javax.inject.Inject

class GetCurrentAddressUseCase @Inject constructor(private val locationRepository: CurrentAddressRepository){


    suspend operator fun invoke(lat: Double, lon: Double) = locationRepository.getCurrentLocation(lat,lon)

}