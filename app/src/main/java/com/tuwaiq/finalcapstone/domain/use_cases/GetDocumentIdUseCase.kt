package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import javax.inject.Inject

class GetDocumentIdUseCase @Inject constructor(private val moodRepo: MoodRepo) {

    suspend operator fun invoke(): List<String> = moodRepo.getDocumentId()
}