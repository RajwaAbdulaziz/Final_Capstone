package com.tuwaiq.finalcapstone.domain.repo

import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.tasks.await

interface UserRepo {

    fun username(myCallback: MyCallback)

    suspend fun updateUsername(newName: String)

    fun updateUserMood(note: Mood)

    fun updateMoodsV(moodsV: String)

    suspend fun checkMoodsV(): String?
}