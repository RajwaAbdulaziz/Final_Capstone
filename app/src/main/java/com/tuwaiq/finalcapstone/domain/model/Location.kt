package com.tuwaiq.finalcapstone.domain.model

import com.google.android.gms.maps.model.LatLng

data class Location (
    val owner: String = "",
    val mood: String = "",
    val latLng: LatLng = LatLng(0.0, 0.0)
        )