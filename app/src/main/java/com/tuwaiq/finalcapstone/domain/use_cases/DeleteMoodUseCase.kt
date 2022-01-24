package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import javax.inject.Inject

class DeleteMoodUseCase @Inject constructor(private val moodRepo: MoodRepo) {

    operator fun invoke(id: String) {
        moodRepo.deleteMood(id)
    }
}