package com.tuwaiq.finalcapstone.domain.use_cases

import android.util.Log
import androidx.lifecycle.LiveData
import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "CheckMoodUseCase"
class CheckMoodUseCase @Inject constructor(private val moodRepo: MoodRepo){

    suspend operator fun invoke(): Flow<Boolean> {
        Log.d(TAG, "b: ${moodRepo.checkIfMoodLoggedToday()}")
        return moodRepo.checkIfMoodLoggedToday()
    }

}