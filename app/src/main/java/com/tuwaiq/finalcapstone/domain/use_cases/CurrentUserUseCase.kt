package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.repo.UserRepo
import javax.inject.Inject

class CurrentUserUseCase @Inject constructor(private val userRepo: UserRepo) {

    suspend operator fun invoke(): String? = userRepo.username()
}