package com.tuwaiq.finalcapstone

import com.tuwaiq.finalcapstone.domain.model.Mood

interface MyCallback {
    fun onCallback(list: List<Mood>)
}