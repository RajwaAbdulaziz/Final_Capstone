package com.tuwaiq.finalcapstone.ui.listFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlin.properties.Delegates

private const val TAG = "ListFragment"
class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var color: String
    private val args: ListFragmentArgs by navArgs()

    private var noteList = mutableListOf<Mood>()

    private var storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        color = args.colorr
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)
        recyclerView = view.findViewById(R.id.moods_rv)
        recyclerView.layoutManager = LinearLayoutManager(context)
        FirebaseUtils().firestoreDatabase.collection("Mood")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        val note = doc.toObject(Mood::class.java)
                        Log.d(TAG, "note: ${Mood().note}")
                        noteList.add(note)
                        //Log.d(TAG, "note: ${note}")
                    }
                    recyclerView.adapter = MoodAdapter(noteList)
                }
            }

        return view
    }
        inner class MoodViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val noteTv: TextView = itemView.findViewById(R.id.note_tv2)
            //private val noteIv: ImageView = itemView.findViewById(R.id.item_iv)

        fun bind(moods: Mood) {
            noteTv.text = moods.note
//            val ref = Firebase.storage.reference.child("pics/${Mood().id}")
//            ref.downloadUrl.addOnSuccessListener {
//                noteIv.setImageURI(it)
//            }


            when(moods.color) {
                "pink" -> noteTv.setTextColor(resources.getColor(R.color.dark_pink))
                "green" -> noteTv.setTextColor(resources.getColor(R.color.dark_green))
                "orange" -> noteTv.setTextColor(resources.getColor(R.color.dark_orange))
                "purple" -> noteTv.setTextColor(resources.getColor(R.color.dark_purple))
            }
        }
    }

    inner class MoodAdapter(var moods: List<Mood>): RecyclerView.Adapter<MoodViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder{
            val view = layoutInflater.inflate(R.layout.moods_list_item, parent, false)
            return MoodViewHolder(view)
        }

        override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
            val mood = moods[position]
            holder.bind(mood)
//            if (position == 0) {
//                recyclerView.setBackgroundColor(resources.getColor(R.color.yellow))
//            } else if (position == 1) {
//                recyclerView.setBackgroundColor(resources.getColor(R.color.blue))
//            } else if (position == 2) {
//                recyclerView.setBackgroundColor(resources.getColor(R.color.red))
//            }
        }

        override fun getItemCount(): Int = moods.size

    }
}