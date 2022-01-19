package com.tuwaiq.finalcapstone.presentation.listFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.repo.Repo
import kotlinx.coroutines.flow.Flow

private const val TAG = "ListViewModel"
class ListViewModel : ViewModel() {

    private var repo = Repo.getInstance()

    suspend fun currentUserName(): String? {
        return repo.userName()
    }

    suspend fun getProfileListOfMoods(): Flow<MutableList<Mood>> {
        return repo.getProfileListOfMoods()
    }

      suspend fun getListOfMoods(): Flow<MutableList<Mood>> {
        return repo.getListOfMoods()
    }
}