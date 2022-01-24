package com.tuwaiq.finalcapstone.presentation.memeApiFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.data.remote.memeApi.models.Meme
import com.tuwaiq.finalcapstone.data.remote.memeApi.repo.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow

class MemeApiViewModel: ViewModel() {

    private val repo = ApiRepo()

    suspend fun getMemes(): Flow<List<Meme>> {
        return repo.getMeme()
    }
}