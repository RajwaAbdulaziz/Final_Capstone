package com.tuwaiq.finalcapstone.ui.loginFragment

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.model.User
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    fun instance(): FirebaseUser? {
        auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }


     fun login(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    throw Exception("wrong login")
                }
            }
    }
}