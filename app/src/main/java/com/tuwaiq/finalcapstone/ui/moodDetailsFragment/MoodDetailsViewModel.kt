package com.tuwaiq.finalcapstone.ui.moodDetailsFragment

import androidx.lifecycle.ViewModel

import com.tuwaiq.finalcapstone.repo.Repo
import java.io.File

class MoodDetailsViewModel : ViewModel() {

    private val repo = Repo.getInstance()

    suspend fun currentUserName(): String? {
        return repo.userName()
    }

}