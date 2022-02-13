package com.tuwaiq.finalcapstone.presentation.chatFragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.databinding.ChatFragmentBinding
import com.tuwaiq.finalcapstone.databinding.ChatMessageItemBinding
import com.tuwaiq.finalcapstone.domain.model.Chat
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.presentation.loginFragment.LoginFragment
import com.tuwaiq.finalcapstone.presentation.moodFragment.MoodFragment
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import com.tuwaiq.finalcapstone.utils.FormatDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import java.util.*

private const val TAG = "ChatFragment"
private var done = false
@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: ChatFragmentBinding
    private lateinit var fab: FloatingActionButton
    private val viewModel by viewModels<ChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChatFragmentBinding.inflate(inflater, container, false)
        binding.listOfMessages.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fab = activity?.findViewById(R.id.fab)!!

        fab.hide()
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseUtils().auth.currentUser == null) {
            val intent = Intent(context, LoginFragment::class.java)
            startActivity(intent)
        } else {
            displayChatMessages()
            if (!done) {
                lifecycleScope.launch {
                    checkMoods()
                }
                done = true
            }
        }

        binding.fab2.setOnClickListener {
            val chat = Chat(
                binding.input.text.toString(),
                "Anonymous",
                FirebaseUtils().auth.currentUser!!.uid
            )
            FirebaseUtils().fireStoreDatabase.collection("Chat").document().set(chat)
            binding.listOfMessages.requestLayout()
            binding.input.hideKeyboard()
            binding.input.text.clear()
        }

//        object : MyCallback {
//            override fun onMoodCallback(list: List<Mood>) {
//                //super.onMoodCallback(list)
//
//                Log.d(TAG, "moodsss: $list")
//                val formatDate = FormatDate()
//                var ifExists = false
//
//                val userList = list.filter {
//                    it.owner == FirebaseUtils().auth.currentUser?.uid
//                }
//
//                userList.forEach {
//                    val chatDate = formatDate(it.date, "d")
//                    val currentDate = formatDate(Date(), "d")
//                    Log.d(TAG, "first: $chatDate")
//                    Log.d(TAG, "second: $currentDate")
//                    if (chatDate == currentDate) {
//                        ifExists = true
//                    }
//                }
//
//                Log.d(TAG, "if: $ifExists")
//                if (!ifExists) {
//
//                }
//            }
//        }
    }

    private suspend fun checkMoods() {
        val formatDate = FormatDate()
        viewModel.getMoodsList(1, object : MyCallback {
            override fun onMoodCallback(list: List<Mood>) {
                val userList = list.filter {
                    formatDate(it.date, "d") == formatDate(Date(), "d")
                }
                if (userList.isEmpty()) {
                    showDialog()
                }
                Log.d(TAG, "hi: $list")
            }
        })
    }

    private fun showDialog() {
        AlertDialog.Builder(context)
            .setMessage(R.string.no_mood)
            .setPositiveButton(
                R.string.log_it,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    findNavController().navigate(R.id.action_chatFragment_to_moodFragment)
                })
            .setNegativeButton(
                R.string.continue_,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
            .create()
            .show()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onStop() {
        super.onStop()

        fab.show()
    }

    private fun displayChatMessages() {
        viewModel.getListOfMessages(object : MyCallback {
            override fun onChatCallback(list: List<Chat>) {
                binding.listOfMessages.adapter = ChatAdapter(list)
            }
        })
    }

    inner class ChatViewHolder(private val binding: ChatMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var chat: Chat

        fun bind(chat: Chat) {
            this.chat = chat

            var m = ""

            binding.messageText.text = chat.message
            binding.messageUser.text = chat.user

            val formatDate = FormatDate()
            binding.messageTime.text = formatDate(chat.time, "LLL dd hh:mm:ss")

            lifecycleScope.launch {
                FirebaseUtils().fireStoreDatabase.collection("Mood")
                    .whereEqualTo("owner", FirebaseUtils().auth.currentUser?.uid)
                    .orderBy("date")
                    .addSnapshotListener { value, error ->
                        value?.forEach {
                            m = it.getString("mood").toString()
                        }
                        Log.d(TAG, "mood : $m")
                    }
            }

                val a = FirebaseUtils().fireStoreDatabase.collection("Chat")

                a.whereEqualTo("userId", FirebaseUtils().auth.currentUser?.uid).get()
                    .addOnCompleteListener {
                        for (doc in it.result) {
                            a.document(doc.id).update("mood", m)
                        }
                    }

            val color: Int = when (chat.mood) {
                "good" -> resources.getColor(R.color.dark_pink)
                "great" -> resources.getColor(R.color.dark_green)
                "sad" -> resources.getColor(R.color.dark_blue)
                "depressed" -> resources.getColor(R.color.darkest_blue)
                "angry" -> resources.getColor(R.color.red)
                "neutral" -> resources.getColor(R.color.grey)
                else -> resources.getColor(R.color.black)
            }
                binding.messageUser.setTextColor(color)
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
