package com.tuwaiq.finalcapstone.domain.use_cases

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.tuwaiq.finalcapstone.domain.model.Location
import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanLongUseCase @Inject constructor(private val moodRepo: MoodRepo){

    suspend operator fun invoke(googleMap: GoogleMap): Flow<List<Location>> = moodRepo.getLanLong(googleMap)
}