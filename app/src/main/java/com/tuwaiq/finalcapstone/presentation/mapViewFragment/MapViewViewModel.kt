package com.tuwaiq.finalcapstone.presentation.mapViewFragment

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.tuwaiq.finalcapstone.domain.model.Location
import com.tuwaiq.finalcapstone.domain.use_cases.GetLanLongUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MapViewViewModel @Inject constructor(private val getLanLongUseCase: GetLanLongUseCase): ViewModel() {

    suspend fun getLatLong(googleMap: GoogleMap): Flow<List<Location>> {
        return getLanLongUseCase(googleMap)
    }
}