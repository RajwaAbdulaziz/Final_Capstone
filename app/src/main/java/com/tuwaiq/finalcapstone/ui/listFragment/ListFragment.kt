package com.tuwaiq.finalcapstone.ui.listFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "ListFragment"
class ListFragment : Fragment() {

    private val listViewModel by lazy { ViewModelProvider(this).get(ListViewModel::class.java) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var color: String
    private val args: ListFragmentArgs by navArgs()

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

        lifecycleScope.launch {
            updateUI()
        }

        return view
    }

    private suspend fun updateUI() {
        recyclerView.adapter = MoodAdapter(listViewModel.getListOfMoods())
    }

    inner class MoodViewHolder(view: View): RecyclerView.ViewHolder(view){

            private val nameTv: TextView = itemView.findViewById(R.id.name_Tv)
            private val noteTv: TextView = itemView.findViewById(R.id.note_tv2)
            private val noteIv: ImageView = itemView.findViewById(R.id.item_iv2)
            private val moodIv: ImageView = itemView.findViewById(R.id.item_iv3)

        fun bind(moods: Mood) {

            lifecycleScope.launch {
                nameTv.text = listViewModel.currentUserName()
            }

            noteTv.text = moods.note

            if (FirebaseUtils().auth.currentUser?.uid == moods.owner) {
                Glide.with(requireContext())
                    .load(moods.pic)
                    .into(noteIv)
                    } else {
                        noteIv.setImageBitmap(null)
            }

            when (moods.mood) {
                "good" -> moodIv.setImageResource(R.drawable.good)
                "great" -> moodIv.setImageResource(R.drawable.great)
                "sad" -> moodIv.setImageResource(R.drawable.sad)
                "depressed" -> moodIv.setImageResource(R.drawable.depressed)
                "angry" -> moodIv.setImageResource(R.drawable.angry)
                "huh" -> moodIv.setImageResource(R.drawable.huh)
                "disgusted" -> moodIv.setImageResource(R.drawable.disgusted)
            }


            when(moods.color) {
                "pink" -> noteTv.setTextColor(resources.getColor(R.color.dark_pink))
                "green" -> noteTv.setTextColor(resources.getColor(R.color.dark_green))
                "orange" -> noteTv.setTextColor(resources.getColor(R.color.dark_orange))
                "purple" -> noteTv.setTextColor(resources.getColor(R.color.dark_purple))
            }
        }
    }

    inner class MoodAdapter(private var moods: List<Mood>): RecyclerView.Adapter<MoodViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder{
            val view = layoutInflater.inflate(R.layout.moods_list_item, parent, false)
            return MoodViewHolder(view)
        }

        override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
            val mood = moods[position]
            holder.bind(mood)
        }

        override fun getItemCount(): Int = moods.size

    }
}