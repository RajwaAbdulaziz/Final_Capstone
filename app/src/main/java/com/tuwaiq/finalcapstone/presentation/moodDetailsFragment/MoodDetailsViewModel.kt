package com.tuwaiq.finalcapstone.presentation.moodDetailsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.model.Mood

import com.tuwaiq.finalcapstone.data.repo.Repo
import com.tuwaiq.finalcapstone.domain.use_cases.AddMoodUseCase
import com.tuwaiq.finalcapstone.domain.use_cases.CurrentUserUseCase
import com.tuwaiq.finalcapstone.domain.use_cases.UpdateUserMoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoodDetailsViewModel @Inject constructor(private val currentUserUseCase: CurrentUserUseCase,
                                               private val addMoodUseCase: AddMoodUseCase,
                                                private val updateUserMoodUseCase: UpdateUserMoodUseCase): ViewModel() {


    fun currentUserName(myCallback: MyCallback) {
        return currentUserUseCase(myCallback)
    }

    fun addMood(note: Mood) {
        addMoodUseCase(note)
    }

    fun updateUserMood(note: Mood){
        updateUserMoodUseCase(note)
    }
}