package com.tuwaiq.finalcapstone.presentation.loginFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.repo.Repo
import com.tuwaiq.finalcapstone.utils.FirebaseUtils

class LoginViewModel : ViewModel() {

    private var repo = Repo.getInstance()


    var currentUser = FirebaseUtils().auth.currentUser

     fun login(email: String, password: String) {
        repo.login(email, password)
    }
}