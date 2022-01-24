package com.tuwaiq.finalcapstone.domain.model

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class Chat (
    var message: String = "",
    var user: String = "",
    var userId: String = "",
    var mood: String = "",
    var time: Date = Date()
)
