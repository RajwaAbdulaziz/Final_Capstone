package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.domain.repo.UserRepo
import javax.inject.Inject

class UpdateUserMoodUseCase @Inject constructor(private val userRepo: UserRepo) {

    operator fun invoke(note: Mood) = userRepo.updateUserMood(note)
}