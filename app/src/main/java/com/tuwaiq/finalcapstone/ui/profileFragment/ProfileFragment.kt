package com.tuwaiq.finalcapstone.ui.profileFragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
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
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.ui.listFragment.ListFragment
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import androidx.core.graphics.drawable.DrawableCompat


import androidx.appcompat.content.res.AppCompatResources




private const val TAG = "ProfileFragment"
class ProfileFragment : Fragment() {

    private val profileViewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }

    private var mood = Mood()

    private lateinit var profileRv: CarouselRecyclerview
    private lateinit var nameTv: TextView
    private lateinit var emailTv: TextView
    private lateinit var moodsSittSwitchCompat: SwitchCompat
    private lateinit var sharedPref: SharedPreferences
    private lateinit var donutView: DonutProgressView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        profileRv = view.findViewById(R.id.profile_rv)
        nameTv = view.findViewById(R.id.personal_name_tv)
        emailTv = view.findViewById(R.id.personal_email_tv)
        //moodsSittSwitchCompat = view.findViewById(R.id.moods_settings_switch)
        donutView = view.findViewById(R.id.donut_view)
        //profileRv.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            updateUI()
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)!!
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        moodsSittSwitchCompat.setOnCheckedChangeListener { compatSwitch, b ->
//            Log.d(TAG, b.toString())
//            val a = sharedPref.edit()?.putBoolean("switch_state", b)?.apply()
//            Log.d(TAG, "put: $a")
//            }
        }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            nameTv.text = profileViewModel.userName()
        }
        emailTv.text = FirebaseUtils().auth.currentUser?.email

            //moodsSittSwitchCompat.isChecked = sharedPref.getBoolean("switch_state", false)
            //Log.d(TAG, "get: ${moodsSittSwitchCompat.isChecked}")

        FirebaseUtils().firestoreDatabase.collection("Mood").get()
            .addOnSuccessListener {
                var good = 0
                var great = 0
                var sad = 0
                var depressed = 0
                var angry = 0
                var neutral = 0
                val set = mutableSetOf<String>()

                it.forEach {
                    when(it.getString("mood")) {
                        "good" -> {
                            good+=1
                            set.add("good")
                        }
                        "great" -> {
                            great+=1
                            set.add("great")
                        }
                        "sad" -> {
                            sad+=1
                            set.add("sad")
                        }
                        "depressed" -> {
                            depressed+=1
                            set.add("depressed")
                        }
                        "angry" -> {
                            angry+=1
                            set.add("angry")
                        }
                        "neutral" -> {
                            neutral+=1
                            set.add("neutral")
                        }
                    }
                }
                val section1 = DonutSection(
                    name = "good",
                    color = Color.parseColor("#F194D1"),
                    amount = good.toFloat()
                )
                val section2 = DonutSection(
                    name = "great",
                    color = Color.parseColor("#59C57D"),
                    amount = great.toFloat()
                )
                val section3 = DonutSection(
                    name = "sad",
                    color = Color.parseColor("#AFD1E1"),
                    amount = sad.toFloat()
                )
                val section4 = DonutSection(
                    name = "depressed",
                    color = Color.parseColor("#4F8EC0"),
                    amount = depressed.toFloat()
                )
                val section5 = DonutSection(
                    name = "angry",
                    color = Color.parseColor("#E82A28"),
                    amount = angry.toFloat()
                )
                val section6 = DonutSection(
                    name = "neutral",
                    color = Color.parseColor("#C1C0C0"),
                    amount = neutral.toFloat()
                )
                donutView.cap = set.size.toFloat()
                Log.d(TAG, "count: ${set.size}")
                donutView.submitData(listOf(section1, section2, section3, section4, section5, section6))
                Log.d(TAG, "great: $great - depressed: $depressed - good: $good - angry: $angry")
            }


        //Log.d(TAG, "greattt: $great")

    }

    private suspend fun updateUI() {
        profileViewModel.getProfileListOfMoods().onEach {
            profileRv.adapter = ProfileAdapter(it)
        }.launchIn(lifecycleScope)
    }

    inner class ProfileViewHolder(view: View): RecyclerView.ViewHolder(view){

        private lateinit var mood: Mood
        private var profileNoteBoundsTv: TextView = itemView.findViewById(R.id.personal_note_bounds_tv)
        private var profileNoteTv: TextView = itemView.findViewById(R.id.personal_note_tv)
        private var profileNoteIv: ImageView = itemView.findViewById(R.id.personal_pic_iv)
        private var profileMoodIv: ImageView = itemView.findViewById(R.id.personal_chosen_mood_iv)
        private val conLayout: ConstraintLayout = itemView.findViewById(R.id.con_layout)
        //private var deleteMood: ImageButton = itemView.findViewById(R.id.personal_delete_mood)

        fun bind(mood: Mood) {
            this.mood = mood

            profileNoteTv.text = mood.note

            val unwrappedDrawable = AppCompatResources.getDrawable(
                context!!, com.tuwaiq.finalcapstone.R.drawable.rect
            )

            Log.d(TAG, mood.mood)
//            when(mood.color) {
//                "pink" -> {
//                    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//                    DrawableCompat.setTint(wrappedDrawable, resources.getColor((com.tuwaiq.finalcapstone.R.color.dark_pink)))
//                }
//                "green" -> {
//                    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//                    DrawableCompat.setTint(wrappedDrawable, resources.getColor((com.tuwaiq.finalcapstone.R.color.dark_green)))
//                }
//                "blue" -> {
//                    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//                    DrawableCompat.setTint(wrappedDrawable, resources.getColor((com.tuwaiq.finalcapstone.R.color.dark_blue)))
//                }
//                "dark_blue" -> {
//                    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//                    DrawableCompat.setTint(wrappedDrawable, resources.getColor((com.tuwaiq.finalcapstone.R.color.darkest_blue)))
//                }
//                "light_red" -> {
//                    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//                    DrawableCompat.setTint(wrappedDrawable, resources.getColor((com.tuwaiq.finalcapstone.R.color.red)))
//                }
//                "light_gray" -> {
//                    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//                    DrawableCompat.setTint(wrappedDrawable, resources.getColor((com.tuwaiq.finalcapstone.R.color.gray)))
//                }
//                else -> {
//
//                }
//            }

            if(mood.pic != "") {
                Glide.with(requireContext())
                    .load(mood.pic)
                    .into(profileNoteIv)
            } else {
                profileNoteIv.visibility = View.GONE
                val n = profileNoteTv.layoutParams
                n.width = 660
                val b = profileNoteBoundsTv.layoutParams
                b.width = 720
            }


            when (mood.mood) {
                "good" -> profileMoodIv.setImageResource(R.drawable.good)
                "great" -> profileMoodIv.setImageResource(R.drawable.great)
                "sad" -> profileMoodIv.setImageResource(R.drawable.sad)
                "depressed" -> profileMoodIv.setImageResource(R.drawable.depressed)
                "angry" -> profileMoodIv.setImageResource(R.drawable.angry)
                "neutral" -> profileMoodIv.setImageResource(R.drawable.neutral)
            }

            when (mood.color) {
                "pink" -> profileNoteTv.setTextColor(resources.getColor(R.color.dark_pink))
                "green" -> profileNoteTv.setTextColor(resources.getColor(R.color.dark_green))
                "orange" -> profileNoteTv.setTextColor(resources.getColor(R.color.dark_orange))
                "purple" -> profileNoteTv.setTextColor(resources.getColor(R.color.dark_purple))
            }
        }
    }

    inner class ProfileAdapter(private var moods: List<Mood>): RecyclerView.Adapter<ProfileViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
            val view = layoutInflater.inflate(R.layout.personal_moods_list_item, parent, false)
            return ProfileViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
            val mood = moods[position]
            holder.bind(mood)
        }

        override fun getItemCount(): Int = moods.size

    }

}