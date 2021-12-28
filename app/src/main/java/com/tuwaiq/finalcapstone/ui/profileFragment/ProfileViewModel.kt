package com.tuwaiq.finalcapstone.ui.profileFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.repo.Repo
import kotlinx.coroutines.flow.Flow


class ProfileViewModel : ViewModel() {

    private val repo = Repo.getInstance()

    suspend fun getProfileListOfMoods(): Flow<MutableList<Mood>> {
        return repo.getProfileListOfMoods()
    }
}