package com.tuwaiq.finalcapstone.presentation.profileFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.data.repo.Repo
import com.tuwaiq.finalcapstone.domain.use_cases.CurrentUserUseCase
import com.tuwaiq.finalcapstone.domain.use_cases.GetProfileMoodsListUseCase
import com.tuwaiq.finalcapstone.domain.use_cases.UpdateUsernameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val getProfileMoodsListUseCase: GetProfileMoodsListUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase)
    : ViewModel() {

    private val repo = Repo.getInstance()

    suspend fun getProfileListOfMoods(): Flow<MutableList<Mood>> {
        return getProfileMoodsListUseCase()
    }

    suspend fun userName(): String? {
        return currentUserUseCase()
    }

    fun updateUsername(newName: String) {
        updateUsernameUseCase(newName)
    }
}