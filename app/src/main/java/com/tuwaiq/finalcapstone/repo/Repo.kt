package com.tuwaiq.finalcapstone.repo

import android.content.Context
import android.util.Log
import android.widget.NumberPicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.UploadTask
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import java.lang.Exception
import kotlinx.coroutines.tasks.await
import java.lang.reflect.InvocationTargetException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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


    //var a: MutableList<Mood> = mutableListOf()

    suspend fun displayMoods(): Flow<Task<QuerySnapshot>> {
        return flow<Task<QuerySnapshot>> {
                FirebaseUtils().firestoreDatabase.collection("Mood").get().addOnCompleteListener {

                }
            }
        }


      suspend fun getListOfMoods(): Flow<MutableList<Mood>> {
          var m: MutableList<Mood>
          return flow {

              val a = FirebaseUtils().firestoreDatabase.collection("Mood").get().await()
              m = a.toObjects(Mood::class.java)
              emit(m)
          }
      }

    suspend fun getProfileListOfMoods(): Flow<MutableList<Mood>> {
        var m: MutableList<Mood>
        return flow {

            val a = FirebaseUtils().firestoreDatabase.collection("Mood")
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