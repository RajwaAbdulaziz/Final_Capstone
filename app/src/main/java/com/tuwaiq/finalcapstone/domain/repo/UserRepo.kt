package com.tuwaiq.finalcapstone.domain.repo

import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.tasks.await

interface UserRepo {

    suspend fun username(): String?

    fun updateUsername(newName: String)

    fun updateUserMood(note: Mood)
}