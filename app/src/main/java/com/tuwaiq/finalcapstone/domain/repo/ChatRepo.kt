package com.tuwaiq.finalcapstone.domain.repo

import com.tuwaiq.finalcapstone.MyCallback

interface ChatRepo {

    fun getListOfMessages(myCallback: MyCallback)
}