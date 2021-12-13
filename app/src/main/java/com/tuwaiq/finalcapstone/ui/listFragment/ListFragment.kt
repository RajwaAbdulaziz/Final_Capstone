package com.tuwaiq.finalcapstone.ui.listFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlin.properties.Delegates

private const val TAG = "ListFragment"
class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var color: String
    private val args: ListFragmentArgs by navArgs()

    private var noteList = mutableListOf<Mood>()

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
        FirebaseUtils().firestoreDatabase.collection("notes")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        val note = doc.toObject(Mood::class.java)
                        noteList.add(note)
                    }
                    recyclerView.adapter = MoodAdapter(noteList)
                }
            }

        return view
    }
        inner class MoodViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val noteTv: TextView = itemView.findViewById(R.id.note_tv)

        fun bind(moods: Mood) {
            noteTv.text = moods.note
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