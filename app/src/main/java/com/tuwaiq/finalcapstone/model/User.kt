package com.tuwaiq.finalcapstone.model

import java.util.*

data class User(
    var name: String = "",
    var email: String = "",
    var note: List<Mood> = listOf()
)