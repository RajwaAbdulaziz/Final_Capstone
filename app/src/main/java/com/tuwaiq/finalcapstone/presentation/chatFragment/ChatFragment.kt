package com.tuwaiq.finalcapstone.presentation.chatFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.databinding.ChatFragmentBinding
import com.tuwaiq.finalcapstone.databinding.ChatMessageItemBinding
import com.tuwaiq.finalcapstone.domain.model.Chat
import com.tuwaiq.finalcapstone.presentation.loginFragment.LoginFragment
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import com.tuwaiq.finalcapstone.utils.FormatDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.ZoneId
import java.util.*

private const val TAG = "ChatFragment"
class ChatFragment : Fragment() {

    private lateinit var binding: ChatFragmentBinding
    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChatFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fab: FloatingActionButton = activity?.findViewById(R.id.fab)!!

        fab.hide()

        if (FirebaseUtils().auth.currentUser == null) {
            val intent = Intent(context, LoginFragment::class.java)
            startActivity(intent)
        } else {
            lifecycleScope.launch {
                displayChatMessages()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.fab2.setOnClickListener {
            val chat = Chat(
                binding.input.text.toString(),
                "Anonymous",
                FirebaseUtils().auth.currentUser!!.uid
            )
            FirebaseUtils().fireStoreDatabase.collection("Chat").document().set(chat)
        }
    }

    private suspend fun displayChatMessages() {
        val a = FirebaseUtils().fireStoreDatabase.collection("Chat").get().await()
        val b: List<Chat> = a.toObjects(Chat::class.java).sortedBy {
            it.time
        }
        binding.listOfMessages.layoutManager = LinearLayoutManager(context)
        binding.listOfMessages.adapter = ChatAdapter(b)
    }

    inner class ChatViewHolder(private val binding: ChatMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var chat: Chat

        fun bind(chat: Chat) {
            this.chat = chat

            var m = ""
            lifecycleScope.launch {
                FirebaseUtils().fireStoreDatabase.collection("Mood").orderBy("date")
                    .limitToLast(1)
                    .whereEqualTo("owner", FirebaseUtils().auth.currentUser?.uid)
                    .get().await().forEach {
                        m = it.getString("mood").toString()
                        Log.d(TAG, "mood : $m")
                    }
//
                val a = FirebaseUtils().fireStoreDatabase.collection("Chat")

                a.whereEqualTo("userId", FirebaseUtils().auth.currentUser?.uid).get()
                    .addOnCompleteListener {
                        for (doc in it.result) {
                            a.document(doc.id).update("mood", m)
                        }
                    }

                var color = 0

                when (chat.mood) {
                    "good" -> color = resources.getColor(R.color.pink)
                    "great" -> color = resources.getColor(R.color.dark_green)
                    "sad" -> color = resources.getColor(R.color.blue)
                    "depressed" -> color = resources.getColor(R.color.dark_blue)
                    "angry" -> color = resources.getColor(R.color.red)
                    "neutral" -> color = resources.getColor(R.color.grey)
                }

                binding.messageText.text = chat.message
                binding.messageUser.text = chat.user
                binding.messageUser.setTextColor(color)
                val formatDate = FormatDate()
                binding.messageTime.text = formatDate(chat.time, "LLL dd hh:mm:ss")
            }
        }
    }

        inner class ChatAdapter(private var chats: List<Chat>) :
            RecyclerView.Adapter<ChatViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
                val binding = ChatMessageItemBinding.inflate(layoutInflater, parent, false)
                return ChatViewHolder(binding)
            }

            override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
                val chat = chats[position]
                holder.bind(chat)
            }

            override fun getItemCount(): Int = chats.size

        }
    }
