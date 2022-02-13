package com.tuwaiq.finalcapstone.presentation.listFragment

import android.animation.LayoutTransition
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import coil.load
import com.bumptech.glide.Glide
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.databinding.ListFragmentBinding
import com.tuwaiq.finalcapstone.databinding.MoodsListItemBinding
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import com.tuwaiq.finalcapstone.utils.FormatDate
import com.tuwaiq.finalcapstone.utils.NotifyWork
import com.vivekkaushik.datepicker.OnDateSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "ListFragment"
var TASK = ""
var bool = false
private const val WORKER_ID = "Workerr"
@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding
    private val listViewModel by viewModels<ListViewModel>()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)!!

    }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = ListFragmentBinding.inflate(inflater, container, false)
            binding.shimmerFrameLayout.startShimmerAnimation()
            recyclerView = binding.moodsRv
            recyclerView.layoutManager = LinearLayoutManager(context)

            lifecycleScope.launch {
                updateUI(-1)
            }
            return binding.root
        }

        override fun onStart() {
            super.onStart()

            val workRequest = PeriodicWorkRequest.Builder(
                NotifyWork::class.java,15, TimeUnit.MINUTES)
                .build()

            Log.d(TAG, "work: $workRequest")

            WorkManager.getInstance(requireContext())
                .enqueueUniquePeriodicWork(WORKER_ID,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )

            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy")
            val formatter2 = DateTimeFormatter.ofPattern("MM")
            val formatter3 = DateTimeFormatter.ofPattern("dd")
            val year = current.format(formatter).toInt()
            val month = current.format(formatter2).toInt()
            val day = current.format(formatter3).toInt()

            val m = GregorianCalendar.getInstance()
            m.time = Date()
            val n = Calendar.getInstance().coerceAtMost(m)
            binding.datePickerTimeline.setActiveDate(n)

            binding.datePickerTimeline.setInitialDate(year, month.minus(1), day.minus(3))

            binding.datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
                override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                    lifecycleScope.launch {
                    updateUI(day)
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

            binding.mapIb.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment2_to_mapViewFragment2)
            }
        }

    private suspend fun updateUI(day: Int) {
        val moodsV = listViewModel.checkMoodsV()
        var newList: List<Mood>
        val formatDate = FormatDate()
        listViewModel.getListOfMoods(-1, object : MyCallback{
            override fun onMoodCallback(list: List<Mood>) {
                newList = if (moodsV == true.toString()) {
                    list.filterNot {
                        it.owner == FirebaseUtils().auth.currentUser?.uid
                    }
                } else {
                    list
                }
                val moodList = newList.filter {mood ->
                    val currentDay = formatDate(Date(), "d")
                    val moodDay = formatDate(mood.date, "d")
                    if (day == -1) {
                        currentDay == moodDay
                    } else {
                        moodDay == day.toString()
                    }
                }
                binding.moodsRv.adapter = MoodAdapter(moodList)
            }
        })

            binding.shimmerFrameLayout.stopShimmerAnimation()
            binding.shimmerFrameLayout.visibility = View.GONE
    }

    inner class MoodViewHolder(private val binding: MoodsListItemBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        AdapterView.OnItemSelectedListener {

        private lateinit var moods: Mood

        init {
            binding.expandIb.setOnClickListener(this)
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        fun bind(moods: Mood) {
            this.moods = moods

            if (moods.owner == FirebaseUtils().auth.currentUser?.uid) {
                binding.spinner.visibility = View.VISIBLE
                //binding.ownerTv.text = moods.current
            }

            binding.spinner.onItemSelectedListener = this

            val delete = arrayOf("", "Delete")

            val aa = context?.let {
                ArrayAdapter(it, android.R.layout.simple_spinner_item, delete) }
            aa?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = aa

                binding.nameTv.text = moods.ownerName

                binding.noteTv.text = moods.note

            if (moods.note != "" || (moods.pic != "" && moods.privatePic != "true") || moods.memePic != "-1") {
                    binding.expandIb.visibility = View.VISIBLE
                    binding.expandIb.setOnClickListener(this)
                }

                Glide.with(requireContext())
                .load(moods.memePic)
                .into(binding.memePicIv)

                if (moods.privatePic == "false") {

                    Glide.with(requireContext())
                        .load(moods.pic)
                        .into(binding.picIv)
                }

                when (moods.mood) {
                    "good" -> binding.chosenMoodIv.setImageResource(R.drawable.good)
                    "great" -> binding.chosenMoodIv.setImageResource(R.drawable.great)
                    "sad" -> binding.chosenMoodIv.setImageResource(R.drawable.sad)
                    "depressed" -> binding.chosenMoodIv.setImageResource(R.drawable.depressed)
                    "angry" -> binding.chosenMoodIv.setImageResource(R.drawable.angry)
                    "neutral" -> binding.chosenMoodIv.setImageResource(R.drawable.neutral)
                }

                when (moods.color) {
                    "pink" -> binding.noteTv.setTextColor(resources.getColor(R.color.dark_pink))
                    "green" -> binding.noteTv.setTextColor(resources.getColor(R.color.dark_green))
                    "blue" -> binding.noteTv.setTextColor(resources.getColor(R.color.dark_blue))
                    "dark_blue" -> binding.noteTv.setTextColor(resources.getColor(R.color.darkest_blue))
                    "light_red" -> binding.noteTv.setTextColor(resources.getColor(R.color.red))
                    "light_gray" -> binding.noteTv.setTextColor(resources.getColor(R.color.gray))
                    else -> binding.noteTv.setTextColor(resources.getColor(R.color.black))
                }
            }

        override fun onClick(p0: View?) {

            when (p0) {
//                itemView -> {
//                    TASK = "UPDATE"
//                    lifecycleScope.launch {
//                        listViewModel.getDocumentId().forEach {
//                            Log.d(TAG, "id: $it")
//                            if (it == moods.moodId) {
//                                sharedPref.edit().putString("moodColor", moods.color).apply()
//                                sharedPref.edit().putString("mood", moods.mood).apply()
//                                sharedPref.edit().putString("moodId", moods.moodId).apply()
//                                findNavController().navigate(R.id.action_listFragment2_to_moodDetailsFragment)
//                            }
//                        }
//                    }
//                }
                binding.expandIb -> {
                    if (!bool) {
                        binding.constraintLayout3.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                        TransitionManager.beginDelayedTransition(binding.constraintLayout3, AutoTransition())

                            if (moods.note != "") {
                                Log.d(TAG, "1")
                                binding.noteBorderCardView.visibility = View.VISIBLE
                            }

                            if ((moods.pic != "" && moods.privatePic != "true") || moods.memePic != "-1") {
                                Log.d(TAG, "2")
                                binding.picIv.visibility = View.VISIBLE
                                binding.memePicIv.visibility = View.VISIBLE
                            }

                        val interpolator = OvershootInterpolator()
                        ViewCompat.animate(binding.expandIb).rotation(180f).withLayer().setDuration(300)
                            .setInterpolator(interpolator).start()

                        binding.expandIb.translationY = -60f
                        bool = true

                    } else if (bool){
                        binding.noteBorderCardView.visibility = View.GONE
                        binding.memePicIv.visibility = View.GONE
                        binding.picIv.visibility = View.GONE

                        val interpolator = OvershootInterpolator()
                        ViewCompat.animate(binding.expandIb).rotation(360f).withLayer().setDuration(300)
                            .setInterpolator(interpolator).start()
                        binding.expandIb.translationY = 0f
                        bool = false
                    }
                    recyclerView.requestLayout()
                }
            }
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when(p2) {
                1 -> {
                    listViewModel.deleteMood(moods.moodId)
//                    lifecycleScope.launch {
//                        updateUI(-1)
//                    }
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }

    inner class MoodAdapter(private var moods: List<Mood>): RecyclerView.Adapter<MoodViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder{
            val binding = MoodsListItemBinding.inflate(layoutInflater,parent, false)
            return MoodViewHolder(binding)
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
            val mood = moods[position]
                holder.bind(mood)
        }

        override fun getItemCount(): Int = moods.size
    }
}

