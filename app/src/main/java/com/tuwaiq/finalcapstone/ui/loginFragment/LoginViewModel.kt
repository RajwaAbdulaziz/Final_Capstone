package com.tuwaiq.finalcapstone.ui.loginFragment

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    fun instance(): FirebaseUser? {
        auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }


     fun login(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
    }
}