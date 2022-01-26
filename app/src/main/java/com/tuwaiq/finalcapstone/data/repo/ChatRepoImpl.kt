package com.tuwaiq.finalcapstone.data.repo

import com.google.firebase.firestore.ktx.toObjects
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.model.Chat
import com.tuwaiq.finalcapstone.domain.repo.ChatRepo
import com.tuwaiq.finalcapstone.utils.FirebaseUtils

class ChatRepoImpl: ChatRepo {
    override fun getListOfMessages(myCallback: MyCallback) {
        var m: List<Chat>
        var b: MutableList<Chat>
        FirebaseUtils().fireStoreDatabase.collection("Chat").addSnapshotListener { value, error ->
            if (value?.isEmpty == false) {
                m = value.toObjects(Chat::class.java)
                m = m.sortedBy {
                    it.time
                }
                if (m.size > 50) {
                    m = m.drop(25)
                    myCallback.onChatCallback(m)
                } else {
                    myCallback.onChatCallback(m)
                }
            }
        }
    }
}