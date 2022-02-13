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

    //private val uid = FirebaseUtils().auth.currentUser?.uid

    override fun username(myCallback: MyCallback) {
        val uid = FirebaseUtils().auth.currentUser?.uid
       FirebaseUtils().fireStoreDatabase.collection("Users").document(uid!!).addSnapshotListener { value, error ->
           myCallback.username(value?.getString("name").toString())
        }
    }

    override suspend fun updateUsername(newName: String) {
        val uid = FirebaseUtils().auth.currentUser?.uid
        FirebaseUtils().fireStoreDatabase.collection("Users").document(uid!!).update("name", newName)

        val a = FirebaseUtils().fireStoreDatabase.collection("Mood").whereEqualTo("owner", uid).get().await()

                a?.forEach {
                    FirebaseUtils().fireStoreDatabase.collection("Mood").document(it.id)
                        .update("ownerName", newName)
                }
            }

    override fun updateUserMood(note: Mood) {
        val uid = FirebaseUtils().auth.currentUser?.uid
        FirebaseUtils().fireStoreDatabase.collection("Users")
            .document(uid!!).update("note", FieldValue.arrayUnion(note))
    }

    override fun updateMoodsV(moodsV: String) {
        val uid = FirebaseUtils().auth.currentUser?.uid
        FirebaseUtils().fireStoreDatabase.collection("Users").document(uid!!).update("moodsV", moodsV)
    }

    override suspend fun checkMoodsV(): String? {
        val uid = FirebaseUtils().auth.currentUser?.uid
        return FirebaseUtils().fireStoreDatabase.collection("Users").document(uid!!)
            .get().await().getString("moodsV")
    }
}