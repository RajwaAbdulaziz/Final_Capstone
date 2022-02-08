package com.tuwaiq.finalcapstone.presentation.moodFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.data.repo.Repo
import com.tuwaiq.finalcapstone.domain.use_cases.CurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoodFragmentViewModel @Inject constructor(private val currentUserUseCase: CurrentUserUseCase): ViewModel() {

    fun userName(myCallback: MyCallback){
        return currentUserUseCase(myCallback)
    }
}