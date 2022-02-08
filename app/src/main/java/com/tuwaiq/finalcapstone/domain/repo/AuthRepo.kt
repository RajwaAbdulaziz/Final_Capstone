package com.tuwaiq.finalcapstone.domain.repo

import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.utils.FirebaseUtils

interface AuthRepo {

    fun login(email: String, password: String, myCallback: MyCallback, auth: FirebaseAuth = FirebaseUtils().auth)

    fun register(name: String, email: String, pass: String, myCallback: MyCallback, auth: FirebaseAuth = FirebaseUtils().auth)
}