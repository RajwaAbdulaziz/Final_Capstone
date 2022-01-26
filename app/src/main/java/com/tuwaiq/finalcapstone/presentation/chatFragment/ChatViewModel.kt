package com.tuwaiq.finalcapstone.presentation.chatFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.use_cases.GetListOfMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val getListOfMessagesUseCase: GetListOfMessagesUseCase): ViewModel() {

    fun getListOfMessages(myCallback: MyCallback) {
        getListOfMessagesUseCase(myCallback)
    }
}