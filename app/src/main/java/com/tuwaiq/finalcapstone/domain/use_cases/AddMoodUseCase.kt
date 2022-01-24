package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import javax.inject.Inject

class AddMoodUseCase @Inject constructor(private val moodRepo: MoodRepo) {

    operator fun invoke(note: Mood) = moodRepo.addMood(note)
}