package com.tuwaiq.finalcapstone.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuwaiq.finalcapstone.memeApi.models.Meme
import com.tuwaiq.finalcapstone.memeApi.repo.ApiRepo
import kotlinx.coroutines.flow.Flow

class MemeApiViewModel : ViewModel() {

    private val repo = ApiRepo()

    private val _result: MutableLiveData<List<Meme>> = MutableLiveData()

    //val result: LiveData<List<Meme>> = _result

    suspend fun getMemes(): Flow<List<Meme>> {
        return repo.getMeme()
    }
}