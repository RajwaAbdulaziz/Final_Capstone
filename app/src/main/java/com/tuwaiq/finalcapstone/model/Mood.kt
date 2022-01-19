package com.tuwaiq.finalcapstone.model

import com.google.type.LatLng
import java.util.*

data class Mood(
    var note: String = "",
    var color: String = "",
    var pic: String = "",
    var mood: String = "",
    var owner: String? = "",
    var ownerName: String = "",
    var memePic: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var moodId: String = "",
    var privatePic: String = "",
    var date: Date = Date()
        )