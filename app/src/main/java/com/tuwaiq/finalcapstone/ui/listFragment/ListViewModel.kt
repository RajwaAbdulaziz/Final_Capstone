package com.tuwaiq.finalcapstone.ui.listFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.repo.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

private const val TAG = "ListViewModel"
class ListViewModel : ViewModel() {

    private var repo = Repo.getInstance()

    suspend fun currentUserName(): String? {
        return repo.userName()
    }

     suspend fun getListOfMoods(): Flow<MutableList<Mood>> {
        return repo.getListOfMoods()
    }
}