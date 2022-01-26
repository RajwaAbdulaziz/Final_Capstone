package com.tuwaiq.finalcapstone.presentation.profileFragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuwaiq.finalcapstone.databinding.PersonalMoodsListItemBinding
import com.tuwaiq.finalcapstone.databinding.ProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ProfileFragment"
@AndroidEntryPoint
class ProfileFragment : Fragment() {


    private lateinit var binding: ProfileFragmentBinding
    private val profileViewModel by viewModels<ProfileViewModel>()

    private var mood = Mood()

    private lateinit var profileRv: CarouselRecyclerview
    private lateinit var nameTv: EditText
    private lateinit var emailTv: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var donutView: DonutProgressView
    private lateinit var editBtn: ImageButton
    private lateinit var doneEditBtn: ImageButton
    private lateinit var logoutBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val view = inflater.inflate(R.layout.profile_fragment, container, false)
        binding = ProfileFragmentBinding.inflate(inflater, container, false)

        //profileRv = view.findViewById(R.id.profile_rv)
//        nameTv = view.findViewById(R.id.personal_name_tv)
//        emailTv = view.findViewById(R.id.personal_email_tv)
//        donutView = view.findViewById(R.id.donut_view)
//        editBtn = view.findViewById(R.id.edit_btn)
//        doneEditBtn = view.findViewById(R.id.done_edit_btn)
//        logoutBtn = view.findViewById(R.id.logout_btn)

        //binding.profileRv.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            updateUI()
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)!!
    }

    override fun onStart() {
        super.onStart()

        binding.logoutBtn.setOnClickListener {
            FirebaseUtils().auth.signOut()
            findNavController().navigate(R.id.action_profileFragment2_to_loginFragment)
        }

        binding.personalNameTv.isCursorVisible = false
        binding.personalNameTv.isFocusableInTouchMode = false

        lifecycleScope.launch {
            binding.personalNameTv.setText(profileViewModel.userName())
        }

        binding.personalEmailTv.setText(FirebaseUtils().auth.currentUser?.email)

        binding.editBtn.setOnClickListener {
            binding.editBtn.visibility = View.INVISIBLE
            binding.doneEditBtn.visibility = View.VISIBLE

            binding.personalNameTv.isCursorVisible = true
            binding.personalNameTv.isFocusableInTouchMode = true
            binding.personalNameTv.inputType = InputType.TYPE_CLASS_TEXT
            binding.personalNameTv.requestFocus()
        }

        binding.doneEditBtn.setOnClickListener {
            binding.doneEditBtn.visibility = View.INVISIBLE
            binding.editBtn.visibility = View.VISIBLE
            binding.personalNameTv.isCursorVisible = false
            binding.personalNameTv.isFocusableInTouchMode = false
            binding.personalNameTv.inputType = InputType.TYPE_NULL
            profileViewModel.updateUsername(binding.personalNameTv.text.toString())
        }
        //moodsSittSwitchCompat.isChecked = sharedPref.getBoolean("switch_state", false)
            //Log.d(TAG, "get: ${moodsSittSwitchCompat.isChecked}")

        FirebaseUtils().auth.currentUser?.uid?.let { id ->
            FirebaseUtils().fireStoreDatabase.collection("Mood").get()
                .addOnSuccessListener {
                    var good = 0
                    var great = 0
                    var sad = 0
                    var depressed = 0
                    var angry = 0
                    var neutral = 0
                    val set = mutableSetOf<String>()

                    it.forEach {
                        if (it.getString("owner") == id) {
                            when (it.getString("mood")) {
                                "good" -> {
                                    good += 1
                                    set.add("good")
                                }
                                "great" -> {
                                    great += 1
                                    set.add("great")
                                }
                                "sad" -> {
                                    sad += 1
                                    set.add("sad")
                                }
                                "depressed" -> {
                                    depressed += 1
                                    set.add("depressed")
                                }
                                "angry" -> {
                                    angry += 1
                                    set.add("angry")
                                }
                                "neutral" -> {
                                    neutral += 1
                                    set.add("neutral")
                                }
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
                    binding.donutView.cap = set.size.toFloat()
                    Log.d(TAG, "count: ${set.size}")
                    binding.donutView.submitData(listOf(section1, section2, section3, section4, section5, section6))
                    Log.d(TAG, "great: $great - depressed: $depressed - good: $good - angry: $angry")
                }
        }


        //Log.d(TAG, "greattt: $great")

    }

    private suspend fun updateUI() {
        profileViewModel.getProfileListOfMoods().onEach {
            binding.profileRv.adapter = ProfileAdapter(it)
        }.launchIn(lifecycleScope)
    }

    inner class ProfileViewHolder(private val binding: PersonalMoodsListItemBinding): RecyclerView.ViewHolder(binding.root){

        private lateinit var mood: Mood

        fun bind(mood: Mood) {
            this.mood = mood

            if (mood.note == "" && mood.pic == "" && mood.memePic == "-1") {
                binding.psNoteBorderCardView.visibility = View.GONE
                binding.psPicIv.visibility = View.GONE
                binding.memePicIv.visibility = View.GONE
                binding.psChosenMoodIv.translationY = 200f
            }

            binding.psNoteTv.text = mood.note

            Glide.with(requireContext())
                .load(mood.pic)
                .into(binding.psPicIv)

            Glide.with(requireContext())
                .load(mood.memePic)
                .into(binding.memePicIv)

            when (mood.mood) {
                "good" -> binding.psChosenMoodIv.setImageResource(R.drawable.good)
                "great" -> binding.psChosenMoodIv.setImageResource(R.drawable.great)
                "sad" -> binding.psChosenMoodIv.setImageResource(R.drawable.sad)
                "depressed" -> binding.psChosenMoodIv.setImageResource(R.drawable.depressed)
                "angry" -> binding.psChosenMoodIv.setImageResource(R.drawable.angry)
                "neutral" -> binding.psChosenMoodIv.setImageResource(R.drawable.neutral)
            }

            when (mood.color) {
                "pink" -> binding.psNoteTv.setTextColor(resources.getColor(R.color.dark_pink))
                "green" -> binding.psNoteTv.setTextColor(resources.getColor(R.color.dark_green))
                "blue" -> binding.psNoteTv.setTextColor(resources.getColor(R.color.dark_blue))
                "dark_blue" -> binding.psNoteTv.setTextColor(resources.getColor(R.color.darkest_blue))
                "light_red" -> binding.psNoteTv.setTextColor(resources.getColor(R.color.red))
                "light_gray" -> binding.psNoteTv.setTextColor(resources.getColor(R.color.grey))
            }
        }
    }

    inner class ProfileAdapter(private var moods: List<Mood>): RecyclerView.Adapter<ProfileViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
            val binding = PersonalMoodsListItemBinding.inflate(layoutInflater,parent, false)
            return ProfileViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
            val mood = moods[position]
            holder.bind(mood)
        }

        override fun getItemCount(): Int = moods.size

    }

}