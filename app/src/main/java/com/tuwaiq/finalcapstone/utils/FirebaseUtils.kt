package com.tuwaiq.finalcapstone.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtils {
    val auth = FirebaseAuth.getInstance()
    val firestoreDatabase = FirebaseFirestore.getInstance()
}