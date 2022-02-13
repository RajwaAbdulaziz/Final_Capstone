package com.tuwaiq.finalcapstone

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.tuwaiq.finalcapstone.domain.model.Chat
import com.tuwaiq.finalcapstone.domain.model.Mood

interface MyCallback {
    fun onMoodCallback(list: List<Mood>){}

    fun onProfileMoodCallback(list: List<Mood>){}

    fun onChatCallback(list: List<Chat>){}

    fun username(name: String) {}

    fun authResult(authResult: Task<AuthResult>) {}
}