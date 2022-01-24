package com.tuwaiq.finalcapstone.domain.repo

import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.finalcapstone.utils.FirebaseUtils

interface AuthRepo {

    fun login(email: String, password: String, auth: FirebaseAuth = FirebaseUtils().auth)

    fun register(name: String, email: String, pass: String, auth: FirebaseAuth = FirebaseUtils().auth)
}