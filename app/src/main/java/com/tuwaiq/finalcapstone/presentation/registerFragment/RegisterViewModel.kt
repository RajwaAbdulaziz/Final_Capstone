package com.tuwaiq.finalcapstone.presentation.registerFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.repo.Repo

class RegisterViewModel : ViewModel() {

    private var repo = Repo.getInstance()

    fun register(name: String, email: String, password: String) {
        repo.register(name, email, password)
    }
}