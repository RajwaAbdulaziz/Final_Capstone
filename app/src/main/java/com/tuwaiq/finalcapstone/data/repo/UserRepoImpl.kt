package com.tuwaiq.finalcapstone.data.repo

import com.google.firebase.firestore.FieldValue
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.domain.repo.UserRepo
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.tasks.await

class UserRepoImpl: UserRepo {

    override suspend fun username(): String? {
        return FirebaseUtils().fireStoreDatabase.collection("Users")
            .document("${FirebaseUtils().auth.currentUser?.uid}").get().await().getString("name")
    }

    override fun updateUsername(newName: String) {
        FirebaseUtils().fireStoreDatabase.collection("Users").document(FirebaseUtils().auth.currentUser!!.uid).update("name", newName)
    }

    override fun updateUserMood(note: Mood) {
        FirebaseUtils().fireStoreDatabase.collection("Users")
            .document(FirebaseUtils().auth.currentUser!!.uid).update("note", FieldValue.arrayUnion(note))
    }
}