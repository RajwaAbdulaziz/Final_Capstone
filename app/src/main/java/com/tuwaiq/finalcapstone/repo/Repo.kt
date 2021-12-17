package com.tuwaiq.finalcapstone.repo

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import java.lang.Exception
import kotlinx.coroutines.tasks.await

class Repo private constructor(context: Context) {

    fun login(email: String, password: String, auth: FirebaseAuth = FirebaseAuth.getInstance()) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    throw Exception("wrong login")
                }
            }
        }

    fun register(name: String, email: String, pass: String, auth: FirebaseAuth = FirebaseAuth.getInstance()) {
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

    suspend fun userName(): String? {
        return FirebaseUtils().firestoreDatabase.collection("Users")
            .document("${FirebaseUtils().auth.currentUser?.uid}").get().await().getString("name")
    }

     suspend fun getListOfMoods(): MutableList<Mood> {
        val snapshot = FirebaseUtils().firestoreDatabase.collection("Mood")
            .get().await()
        return snapshot.toObjects(Mood::class.java)
    }


    companion object{
        private var INSTANCE:Repo? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = Repo(context)
            }
        }

        fun getInstance():Repo = INSTANCE ?: throw IllegalStateException("Initialize your repo first")
    }
}