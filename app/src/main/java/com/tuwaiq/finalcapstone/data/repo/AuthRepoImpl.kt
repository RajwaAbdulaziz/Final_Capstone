package com.tuwaiq.finalcapstone.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.domain.model.User
import com.tuwaiq.finalcapstone.domain.repo.AuthRepo
import java.lang.Exception

class AuthRepoImpl: AuthRepo {

    override fun login(email: String, password: String, auth: FirebaseAuth) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    throw Exception("wrong login")
                }
            }
    }

    override fun register(name: String, email: String, pass: String, auth: FirebaseAuth) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    val users = User(name, email)
                    val db = FirebaseFirestore.getInstance()
                    db.collection("Users")
                        .document(user!!.uid).set(users)
                    return@addOnCompleteListener
                } else {
                    throw Exception("wrong register")
                }
            }
    }
}

