package com.tuwaiq.finalcapstone.presentation.chatFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.use_cases.GetListOfMessagesUseCase
import com.tuwaiq.finalcapstone.domain.use_cases.GetMoodsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val getListOfMessagesUseCase: GetListOfMessagesUseCase,
                                        private val getMoodsListUseCase: GetMoodsListUseCase): ViewModel() {

    fun getListOfMessages(myCallback: MyCallback) {
        getListOfMessagesUseCase(myCallback)
    }

    suspend fun getMoodsList(day: Int, myCallback: MyCallback) {
        getMoodsListUseCase(day, myCallback)
    }
}