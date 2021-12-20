package com.tuwaiq.finalcapstone.repo

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import kotlinx.coroutines.tasks.await
import java.lang.reflect.InvocationTargetException

private const val TAG = "Repo"
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

     suspend fun getListOfMoods(): Flow<MutableList<Mood>> {
         return flow {
             val datalist = mutableListOf<Mood>()
             FirebaseUtils().firestoreDatabase.collection("Mood")
                 .get().await().forEach {
                     val mood = it.toObject(Mood::class.java)
                     mood.moodId = it.id
                     datalist+=mood
                 }

             emit(datalist)
         }.flowOn(Dispatchers.IO)
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