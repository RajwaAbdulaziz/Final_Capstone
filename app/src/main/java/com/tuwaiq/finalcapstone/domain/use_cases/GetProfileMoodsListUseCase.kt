package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileMoodsListUseCase @Inject constructor(private val moodRepo: MoodRepo) {

    suspend operator fun invoke(): Flow<MutableList<Mood>> = moodRepo.getProfileListOfMoods()
}