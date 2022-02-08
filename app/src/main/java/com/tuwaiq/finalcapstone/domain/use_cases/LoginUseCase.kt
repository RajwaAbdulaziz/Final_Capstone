package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.repo.AuthRepo
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepo: AuthRepo) {

    operator fun invoke(email: String, password: String, myCallback: MyCallback) = authRepo.login(email, password, myCallback)
}