package com.tuwaiq.finalcapstone.presentation.profileFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.data.repo.Repo
import com.tuwaiq.finalcapstone.domain.use_cases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val getProfileMoodsListUseCase: GetProfileMoodsListUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateMoodsVUseCase: UpdateMoodsVUseCase,
    private val checkMoodsVUseCase: CheckMoodsVUseCase)
    : ViewModel() {

    private val repo = Repo.getInstance()

    suspend fun getProfileListOfMoods(): Flow<MutableList<Mood>> {
        return getProfileMoodsListUseCase()
    }

    fun userName(myCallback: MyCallback) {
        return currentUserUseCase(myCallback)
    }

    suspend fun updateUsername(newName: String) {
        updateUsernameUseCase(newName)
    }

    fun updateMoodsV(moodsV: String) {
        updateMoodsVUseCase(moodsV)
    }

    suspend fun checkMoodsV(): String? {
        return checkMoodsVUseCase()
    }
}