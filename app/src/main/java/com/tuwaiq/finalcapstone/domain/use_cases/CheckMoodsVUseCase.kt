package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.repo.UserRepo
import javax.inject.Inject

class CheckMoodsVUseCase @Inject constructor(private val userRepo: UserRepo) {

    suspend operator fun invoke(): String? = userRepo.checkMoodsV()
}