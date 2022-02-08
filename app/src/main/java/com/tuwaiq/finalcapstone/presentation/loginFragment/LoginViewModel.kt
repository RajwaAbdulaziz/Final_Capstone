package com.tuwaiq.finalcapstone.presentation.loginFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.data.repo.Repo
import com.tuwaiq.finalcapstone.domain.use_cases.LoginUseCase
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private var repo = Repo.getInstance()

    var currentUser = FirebaseUtils().auth.currentUser

     fun login(email: String, password: String, myCallback: MyCallback) {
         loginUseCase(email, password, myCallback)
        //repo.login(email, password)
    }
}