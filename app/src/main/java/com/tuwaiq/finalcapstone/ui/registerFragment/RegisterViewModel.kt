package com.tuwaiq.finalcapstone.ui.registerFragment

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.finalcapstone.repo.Repo
import com.tuwaiq.finalcapstone.utils.FirebaseUtils

class RegisterViewModel : ViewModel() {

    private var repo = Repo.getInstance()

    fun register(name: String, email: String, password: String) {
        repo.register(name, email, password)
    }
}