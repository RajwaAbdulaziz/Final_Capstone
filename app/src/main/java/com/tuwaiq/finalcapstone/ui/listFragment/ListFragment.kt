package com.tuwaiq.finalcapstone.ui.listFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "ListFragment"
var TASK = ""
class ListFragment : Fragment() {

    private val listViewModel by lazy { ViewModelProvider(this).get(ListViewModel::class.java) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
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
        //refreshImageButton = view.findViewById(R.id.refresh_ib)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        lifecycleScope.launch {
            updateUI()
        }
//        refreshImageButton.setOnClickListener {
//            lifecycleScope.launch {
//                updateUI()
//            }
//        }
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                updateUI()
                swipeRefreshLayout.isRefreshing = false
            }
        }

        return view
    }

    private suspend fun updateUI() {
        listViewModel.getListOfMoods().onEach {

            recyclerView.adapter = MoodAdapter(it)
            recyclerView.adapter!!.notifyDataSetChanged()

        }.launchIn(lifecycleScope)
//        listViewModel.getListOfMoods().asLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            recyclerView.adapter = MoodAdapter(it)
//        })

    }


    inner class MoodViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var moods: Mood
        private val nameTv: TextView = itemView.findViewById(R.id.name_Tv)
        private val ownerTv: TextView = itemView.findViewById(R.id.owner_tv)
        private val noteTv: TextView = itemView.findViewById(R.id.note_tv2)
        private val noteIv: ImageView = itemView.findViewById(R.id.item_iv2)
        private val moodIv: ImageView = itemView.findViewById(R.id.item_iv3)
        private val deleteMood: ImageButton = itemView.findViewById(R.id.delete_mood)

        init {
            itemView.setOnClickListener(this)
            deleteMood.setOnClickListener(this)
        }

        fun bind(moods: Mood) {
            this.moods = moods

            nameTv.text = moods.ownerName
            noteTv.text = moods.note

            if (FirebaseUtils().auth.currentUser?.uid == moods.owner) {

                ownerTv.text = getString(R.string.you)
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
                "neutral" -> moodIv.setImageResource(R.drawable.neutral)
            }

            when (moods.color) {
                "pink" -> noteTv.setTextColor(resources.getColor(R.color.dark_pink))
                "green" -> noteTv.setTextColor(resources.getColor(R.color.dark_green))
                "orange" -> noteTv.setTextColor(resources.getColor(R.color.dark_orange))
                "purple" -> noteTv.setTextColor(resources.getColor(R.color.dark_purple))
            }
        }

        override fun onClick(p0: View?) {
            val ref = FirebaseUtils().firestoreDatabase.collection("Mood")

            when (p0) {
                deleteMood -> {
                    ref.document(moods.moodId).delete()
                }
                itemView -> {
                    TASK = "UPDATE"
                    ref.get().addOnSuccessListener {
                        it.forEach { documentId ->
                            if (documentId.id == moods.moodId) {
                                Log.d(TAG, "colorrrr ${moods.color}")
                                val action = ListFragmentDirections.actionListFragment2ToMoodDetailsFragment(moods.color, moods.mood, moods.moodId)
                                findNavController().navigate(action)
                            }
                        }
                    }
                }
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