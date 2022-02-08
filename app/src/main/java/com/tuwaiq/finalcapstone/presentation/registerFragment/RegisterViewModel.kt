package com.tuwaiq.finalcapstone.presentation.registerFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.data.repo.Repo
import com.tuwaiq.finalcapstone.domain.use_cases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private var repo = Repo.getInstance()

    fun register(name: String, email: String, password: String, myCallback: MyCallback) {
        registerUseCase(name, email, password, myCallback)
        //repo.register(name, email, password)
    }
}