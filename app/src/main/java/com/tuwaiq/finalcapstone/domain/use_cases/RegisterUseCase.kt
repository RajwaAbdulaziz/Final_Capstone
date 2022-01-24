package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.domain.repo.AuthRepo
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepo: AuthRepo) {

    operator fun invoke(name: String, email: String, password: String) = authRepo.register(name, email, password)
}