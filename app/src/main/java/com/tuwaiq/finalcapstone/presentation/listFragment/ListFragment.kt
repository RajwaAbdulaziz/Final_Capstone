package com.tuwaiq.finalcapstone.presentation.listFragment

import android.animation.LayoutTransition
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import com.vivekkaushik.datepicker.DatePickerTimeline
import com.vivekkaushik.datepicker.OnDateSelectedListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG = "ListFragment"
var TASK = ""
var bool = false
var bool1 = false
class ListFragment : Fragment() {

    private val listViewModel by lazy { ViewModelProvider(this).get(ListViewModel::class.java) }

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    private lateinit var recyclerView: RecyclerView
    private lateinit var color: String

    //private lateinit var fab: FloatingActionButton
    private lateinit var mapIb: ImageButton
    private lateinit var sharedPref: SharedPreferences
    private lateinit var fab: FloatingActionButton

    private lateinit var datePicker: DatePickerTimeline
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private val args: ListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)!!

    }


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.list_fragment, container, false)
            recyclerView = view.findViewById(R.id.moods_rv)
            //fab = MainActivity().findViewById(R.id.fab)
            mapIb = view.findViewById(R.id.map_ib)
            datePicker = view.findViewById(R.id.datePickerTimeline)
            shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout)

            shimmerFrameLayout.startShimmerAnimation()
//
//        recyclerView.visibility = View.GONE
            //recyclerView.layoutManager = linearLayoutManager // Add your recycler view to this ZoomRecycler layout

            recyclerView.layoutManager = LinearLayoutManager(context)
            //swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)


            lifecycleScope.launch {
                Log.d(TAG, "HERE")
                updateUI3()
            }

//        swipeRefreshLayout.setOnRefreshListener {
//            lifecycleScope.launch {
//                updateUI(mutableListOf(Mood()))
//            }
//            swipeRefreshLayout.isRefreshing = false
//        }
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            //fab = requireActivity().findViewById(R.id.fab)
            // fab.show()
        }

        override fun onStart() {
            super.onStart()

            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy")
            val formatter2 = DateTimeFormatter.ofPattern("MM")
            val formatter3 = DateTimeFormatter.ofPattern("dd")
            val year = current.format(formatter).toInt()
            val month = current.format(formatter2).toInt()
            val day = current.format(formatter3).toInt()
            Log.d(TAG, "daaay: ${year}")
            datePicker.setInitialDate(year, month.minus(1), day.minus(3))
            val m = GregorianCalendar.getInstance()
            m.time = Date()
            val n = Calendar.getInstance().coerceAtMost(m)
            datePicker.setActiveDate(n)

            datePicker.setOnDateSelectedListener(object : OnDateSelectedListener {
                override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                    lifecycleScope.launch {
                    updateUI2(year, month, day)
                }

                }

                override fun onDisabledDateSelected(
                    year: Int,
                    month: Int,
                    day: Int,
                    dayOfWeek: Int,
                    isDisabled: Boolean
                ) {
                    TODO("Not yet implemented")
                }


            })

//        datePicker.setOnDateSelectedListener(object: OnDateSelectedListener{
//            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
//                //Log.d(TAG, "day: $day")
//
//                lifecycleScope.launch {
//                    updateUI2(year, month, day)
//                }
//
//                }
//            override fun onDisabledDateSelected(
//                year: Int,
//                month: Int,
//                day: Int,
//                dayOfWeek: Int,
//                isDisabled: Boolean
//            ) {
//            }
//
//                    )
//
//        })

            mapIb.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment2_to_mapViewFragment2)
            }
        }

    private suspend fun updateUI3() {


        listViewModel.getListOfMoods().onEach {
//                shimmerFrameLayout.visibility = View.INVISIBLE
//                recyclerView.visibility = View.VISIBLE

            val m = it.filter { mood ->
                mood.date.day == Date().day
            }

            Log.d(TAG, "LIST: $m")
            recyclerView.adapter = MoodAdapter(m)

            recyclerView.requestLayout()
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }.launchIn(lifecycleScope)
    }

    private suspend fun updateUI2(year: Int, month: Int, day: Int) {

        listViewModel.getListOfMoods().onEach {

            var formatted = ""

            var s = mutableListOf<Mood>()

                s = it.filter {
                    var spf = SimpleDateFormat("E LLL dd hh:mm:ss z yyyy")
                    val parsed = spf.parse(it.date.toString())
                    spf = SimpleDateFormat("d")
                    formatted = spf.format(parsed)
                    formatted == day.toString()
                } as MutableList<Mood>

            Log.d(TAG, formatted)
            //Log.d(TAG, "LISTTTT: $s")
            recyclerView.adapter = MoodAdapter(s)

            //Log.d(TAG, "LIST: $m")

            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE
            recyclerView.adapter!!.notifyDataSetChanged()
        }.launchIn(lifecycleScope)
    }


//        listViewModel.getListOfMoods().asLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            recyclerView.adapter = MoodAdapter(it)
//        })



    inner class MoodViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener,
        AdapterView.OnItemSelectedListener {

        private lateinit var moods: Mood
        private val nameTv: TextView = itemView.findViewById(R.id.name_Tv)
        private val ownerTv: TextView = itemView.findViewById(R.id.owner_tv)
        private val noteBoundsTv: TextView = itemView.findViewById(R.id.note_bounds_tv)
        private val noteTv: TextView = itemView.findViewById(R.id.note_tv)
        private val noteIv: ImageView = itemView.findViewById(R.id.pic_iv)
        private val moodIv: ImageView = itemView.findViewById(R.id.chosen_mood_iv)
        //private val deleteMood: ImageButton = itemView.findViewById(R.id.delete_mood)
        private val memePicIv: ImageView = itemView.findViewById(R.id.meme_pic_iv)
        private val expandIb: ImageButton = itemView.findViewById(R.id.expand_ib)
        private val layout: ConstraintLayout = itemView.findViewById(R.id.constraint_layout_3)
        private val spinner: Spinner = itemView.findViewById(R.id.spinner)

        private val ref = FirebaseUtils().firestoreDatabase.collection("Mood")

        init {

            itemView.setOnClickListener(this)
            //deleteMood.setOnClickListener(this)
            if (FirebaseUtils().auth.uid == FirebaseUtils().auth.currentUser?.uid) {
                spinner.visibility = View.VISIBLE
            }
            spinner.onItemSelectedListener = this
            expandIb.setOnClickListener(this)

            val delete = arrayOf("", "Delete")

        val aa = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, delete) }
        aa?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa

        }


        @RequiresApi(Build.VERSION_CODES.Q)
        fun bind(moods: Mood) {
            this.moods = moods

                nameTv.text = moods.ownerName

                noteTv.text = moods.note

            if (moods.owner == FirebaseUtils().auth.currentUser?.uid) {
                ownerTv.visibility = View.VISIBLE
                ownerTv.text = getString(R.string.you)
            }

            if (moods.note != "" || moods.pic != "" || moods.memePic != "-1") {

                if (moods.owner == FirebaseUtils().auth.currentUser?.uid) {
                    Log.d(TAG, "true")
                    expandIb.visibility = View.VISIBLE
                    expandIb.setOnClickListener(this)
                }
            }

                if (moods.privatePic == "false") {

                    Glide.with(requireContext())
                        .load(moods.pic)
                        .into(noteIv)
                }

            noteIv.load(moods.pic)

           // Log.d(TAG, moods.memePic)

//                if (moods.privatePic == "true" || (moods.pic == "" && moods.memePic == "-1" && moods.note == "")) {
//                    noteIv.visibility = View.GONE
//                    expandIb.visibility = View.GONE
////                    val n = noteTv.layoutParams
////                    n.width = 660
////                    val b = noteBoundsTv.layoutParams
////                    b.width = 720
//                }

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
                    "blue" -> noteTv.setTextColor(resources.getColor(R.color.dark_blue))
                    "dark_blue" -> noteTv.setTextColor(resources.getColor(R.color.darkest_blue))
                    "light_red" -> noteTv.setTextColor(resources.getColor(R.color.red))
                    "light_gray" -> noteTv.setTextColor(resources.getColor(R.color.gray))
                    else -> noteTv.setTextColor(resources.getColor(R.color.black))
                }
            }

        override fun onClick(p0: View?) {

            when (p0) {
//                deleteMood -> {
//                    ref.document(moods.moodId).delete()
//                    lifecycleScope.launch {
//                        updateUI()
//                    }
//                }

                itemView -> {
                    TASK = "UPDATE"
                    ref.get().addOnSuccessListener {
                        it.forEach { documentId ->
                            if (documentId.id == moods.moodId) {

                                sharedPref.edit().putString("moodId", moods.color).apply()
                                sharedPref.edit().putString("moodId", moods.mood).apply()
                                sharedPref.edit().putString("moodId", moods.moodId).apply()
                                //Log.d(TAG, "mooood: ${moods.moodId}")
                                val action = ListFragmentDirections.actionListFragment2ToMoodDetailsFragment(moods.color, moods.mood, moods.moodId)
                                findNavController().navigate(R.id.action_listFragment2_to_moodDetailsFragment)
                            }
                        }
                    }
                }
                expandIb -> {
                    if (!bool1) {
                        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                        TransitionManager.beginDelayedTransition(layout, AutoTransition())
                        if(noteTv.text != null) {
                            noteBoundsTv.visibility = View.VISIBLE
                            noteTv.visibility = View.VISIBLE
                        }
                        if (moods.memePic.isNotEmpty()) {
                            memePicIv.visibility = View.VISIBLE
                        }
                        if (moods.pic.isNotEmpty()) {
                            noteIv.visibility = View.VISIBLE
                        }
//                        val l = layout.layoutParams
//                        l.height = 910
//                        l.width = 900
//                        memePicIv.load(moods.memePic)
//                        if (moods.privatePic != "true") {
//                            noteIv.load(moods.pic)
//                        }

                        val interpolator = OvershootInterpolator()
                        ViewCompat.animate(expandIb).rotation(180f).withLayer().setDuration(300)
                            .setInterpolator(interpolator).start()
                        bool1 = true
                    } else if (bool1){
                        noteBoundsTv.visibility = View.GONE
                        noteTv.visibility = View.GONE
                        memePicIv.visibility = View.GONE
                        noteIv.visibility = View.GONE
//                        val l = layout.layoutParams
//                        l.height = 410
//                        l.width = 900
                        val interpolator = OvershootInterpolator()
                        ViewCompat.animate(expandIb).rotation(360f).withLayer().setDuration(300)
                            .setInterpolator(interpolator).start()
                        bool1 = false
                    }
                    recyclerView.requestLayout()
                }
            }
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when(p2) {
                1 -> {
                    ref.document(moods.moodId).delete()
                    lifecycleScope.launch {
                        updateUI3()
                    }
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }

    inner class MoodAdapter(private var moods: List<Mood>): RecyclerView.Adapter<MoodViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder{
            val view = layoutInflater.inflate(R.layout.moods_list_item, parent, false)
            return MoodViewHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
            val mood = moods[position]
//            val sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)
//            val moodsSettings = sharedPref?.getBoolean("switch_state", false)
//            if (!moodsSettings!!) {
                holder.bind(mood)
//            }
        }

        override fun getItemCount(): Int = moods.size
    }
}

