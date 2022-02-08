package com.tuwaiq.finalcapstone.domain.model

data class User(
    var name: String = "",
    var email: String = "",
    var moodsV: String = "",
    var note: List<Mood> = listOf()
)