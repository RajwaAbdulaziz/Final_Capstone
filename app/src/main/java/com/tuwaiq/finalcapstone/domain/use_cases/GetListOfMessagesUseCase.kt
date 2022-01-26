package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.repo.ChatRepo
import javax.inject.Inject

class GetListOfMessagesUseCase @Inject constructor(private val chatRepo: ChatRepo){

    operator fun invoke(callback: MyCallback) = chatRepo.getListOfMessages(callback)
}