package com.tuwaiq.finalcapstone.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.model.User
import com.tuwaiq.finalcapstone.domain.repo.AuthRepo
import java.lang.Exception

class AuthRepoImpl: AuthRepo {

    override fun login(email: String, password: String, myCallback: MyCallback, auth: FirebaseAuth) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                        myCallback.authResult(it)
            }
        }

    override fun register(name: String, email: String, pass: String, myCallback: MyCallback, auth: FirebaseAuth) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                myCallback.authResult(it)
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    val users = User(name, email, "false")
                    val db = FirebaseFirestore.getInstance()
                    db.collection("Users")
                        .document(user!!.uid).set(users)
                }
            }
        }
    }

