package com.tuwaiq.finalcapstone.presentation.moodFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.repo.Repo

class MoodFragmentViewModel: ViewModel() {

    private val repo = Repo.getInstance()

    suspend fun userName(): String? {
        return repo.userName()
    }
}