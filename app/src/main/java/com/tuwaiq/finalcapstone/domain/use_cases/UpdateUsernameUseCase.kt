package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.repo.UserRepo
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(private val userRepo: UserRepo) {

    operator fun invoke(newName: String) = userRepo.updateUsername(newName)
}