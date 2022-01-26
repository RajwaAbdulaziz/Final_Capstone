package com.tuwaiq.finalcapstone

import com.tuwaiq.finalcapstone.domain.model.Chat
import com.tuwaiq.finalcapstone.domain.model.Mood

interface MyCallback {
    fun onMoodCallback(list: List<Mood>){}

    fun onChatCallback(list: List<Chat>){}
}