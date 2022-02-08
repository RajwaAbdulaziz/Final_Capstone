package com.tuwaiq.finalcapstone.data.repo

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.domain.repo.UserRepo
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.tasks.await

private const val TAG = "UserRepoImpl"
class UserRepoImpl: UserRepo {

    private val uid = FirebaseUtils().auth.currentUser?.uid

    override fun username(myCallback: MyCallback) {
       FirebaseUtils().fireStoreDatabase.collection("Users").document("$uid").addSnapshotListener { value, error ->
           myCallback.username(value?.getString("name").toString())
           Log.d(TAG, "id: ${FirebaseUtils().auth.currentUser?.uid}")
           Log.d(TAG, "username: ${value?.getString("name")}")
        }
    }

    override fun updateUsername(newName: String) {
        FirebaseUtils().fireStoreDatabase.collection("Users").document("$uid").update("name", newName)
    }

    override fun updateUserMood(note: Mood) {
        FirebaseUtils().fireStoreDatabase.collection("Users")
            .document("$uid").update("note", FieldValue.arrayUnion(note))
    }

    override fun updateMoodsV(moodsV: String) {
        FirebaseUtils().fireStoreDatabase.collection("Users").document("$uid").update("moodsV", moodsV)
    }

    override suspend fun checkMoodsV(): String? {
        return FirebaseUtils().fireStoreDatabase.collection("Users").document("$uid")
            .get().await().getString("moodsV")
    }
}