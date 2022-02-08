package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.repo.UserRepo
import javax.inject.Inject

class UpdateMoodsVUseCase @Inject constructor(private val userRepo: UserRepo) {

    operator fun invoke(moodsV: String) = userRepo.updateMoodsV(moodsV)
}