package com.tuwaiq.finalcapstone.ui.loginFragment

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.repo.Repo
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private var repo = Repo.getInstance()


    var currentUser = FirebaseUtils().auth.currentUser

     fun login(email: String, password: String) {
        repo.login(email, password)
    }
}