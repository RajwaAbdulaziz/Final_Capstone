package com.tuwaiq.finalcapstone.presentation.moodDetailsFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.model.Mood

import com.tuwaiq.finalcapstone.repo.Repo
import kotlinx.coroutines.flow.Flow

class MoodDetailsViewModel : ViewModel() {

    private val repo = Repo.getInstance()

    suspend fun currentUserName(): String? {
        return repo.userName()
    }

    suspend fun getListOfMoods(): Flow<MutableList<Mood>> {
        return repo.getListOfMoods()
    }
}