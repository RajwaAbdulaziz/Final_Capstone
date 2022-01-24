package com.tuwaiq.finalcapstone.data.repo

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.domain.model.User
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import kotlinx.coroutines.tasks.await
import java.util.*

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
                    val db = FirebaseUtils().fireStoreDatabase
                    db.collection("Users")
                        .document(user!!.uid).set(users)
                    return@addOnCompleteListener
                } else {
                    throw Exception("wrong register")
                }
            }
    }

    suspend fun userName(): String? {
        return FirebaseUtils().fireStoreDatabase.collection("Users")
            .document("${FirebaseUtils().auth.currentUser?.uid}").get().await().getString("name")
    }


    //var a: MutableList<Mood> = mutableListOf()

    suspend fun displayMoods(): Flow<Task<QuerySnapshot>> {
        return flow<Task<QuerySnapshot>> {
                FirebaseUtils().fireStoreDatabase.collection("Mood").get().addOnCompleteListener {

                }
            }
        }


      suspend fun getListOfMoods(): Flow<MutableList<Mood>> {
          var m: MutableList<Mood>
          return flow {

              val a = FirebaseUtils().fireStoreDatabase.collection("Mood").get().await()
              m = a.toObjects(Mood::class.java)
              emit(m)
          }
      }

    suspend fun getProfileListOfMoods(): Flow<MutableList<Mood>> {
        var m: MutableList<Mood>
        return flow {

            val a = FirebaseUtils().fireStoreDatabase.collection("Mood")
                .whereEqualTo("owner", FirebaseUtils().auth.currentUser?.uid).get().await()
            m = a.toObjects(Mood::class.java)
            emit(m)
        }
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