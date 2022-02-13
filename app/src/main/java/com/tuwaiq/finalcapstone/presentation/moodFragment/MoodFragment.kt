package com.tuwaiq.finalcapstone.presentation.moodFragment

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.databinding.FragmentMoodBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.TimeSource

private const val TAG = "MoodFragment"
private var color = ""
private var mood = ""
@AndroidEntryPoint
class MoodFragment : Fragment() {

    private lateinit var binding: FragmentMoodBinding
    private val moodFragmentViewModel by viewModels<MoodFragmentViewModel>()
    private lateinit var greetingsTv: TextView
    private lateinit var layout: LinearLayout
    private lateinit var goodImageBtn: ImageButton
    private lateinit var greatImageBtn: ImageButton
    private lateinit var sadImageBtn: ImageButton
    private lateinit var depressedImageBtn: ImageButton
    private lateinit var angryImageBtn: ImageButton
    private lateinit var neutralImageButton: ImageButton
    private lateinit var chosenMoodImageView: ImageView
    private lateinit var nextArrow: ImageButton
    private lateinit var sharedPref: SharedPreferences
    private var userName = ""


    private val pink: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.pink)
    }
    private val green: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.green)
    }
    private val blue: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.blue)
    }
    private val darkBlue: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.dark_blue)
    }
    private val lightRed: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.light_red)
    }
    private val lightGray: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.light_gray)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mood, container, false)
        binding = FragmentMoodBinding.inflate(inflater, container, false)
        init(view)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)!!
    }

    private fun init(view: View) {
        greetingsTv = view.findViewById(R.id.greetings_tv)
        layout = view.findViewById(R.id.linear_layout)
        goodImageBtn = view.findViewById(R.id.good_image_btn)
        greatImageBtn = view.findViewById(R.id.great_image_btn)
        sadImageBtn = view.findViewById(R.id.sad_image_btn)
        depressedImageBtn = view.findViewById(R.id.depressed_image_btn)
        angryImageBtn = view.findViewById(R.id.angry_image_btn)
        neutralImageButton = view.findViewById(R.id.neutral_image_btn)
        chosenMoodImageView = view.findViewById(R.id.chosen_mood_image_btn)
        nextArrow = view.findViewById(R.id.next_arrow)
    }

    override fun onStart() {
        super.onStart()

        binding.linearLayout.setBackgroundColor(resources.getColor(R.color.white))


            moodFragmentViewModel.userName(object : MyCallback{
                override fun username(name: String) {
                    super.username(name)

                    Log.d(TAG, "time: ${LocalTime.now()}")
                    val greeting = when {
                        LocalTime.now().isAfter(LocalTime.MIDNIGHT) && LocalTime.now()
                            .isBefore(LocalTime.NOON) -> resources.getString(R.string.morning)
                        LocalTime.now().isAfter(LocalTime.NOON) && LocalTime.now()
                            .isBefore(LocalTime.MIDNIGHT) -> resources.getString(R.string.evening)
                        else -> resources.getString(R.string.afternoon)
                    }
                    Log.d(TAG, "n: ${LocalTime.NOON}")
                    Log.d(TAG, "m: ${LocalTime.MIDNIGHT}")
                    Log.d(TAG, "g: $greeting")
                    binding.greetingsTv.text =
                        resources.getString(R.string.greetings, greeting, name)
                }})

       binding.goodImageBtn.setOnClickListener {

            when (color) {
                "green" -> {
                    moodColorAnimation(green, pink)
                }
                "blue" -> {
                    moodColorAnimation(blue, pink)
                }
                "dark_blue" -> {
                    moodColorAnimation(darkBlue, pink)
                }
                "light_red" -> {
                    moodColorAnimation(lightRed, pink)
                }
                "light_gray" -> {
                    moodColorAnimation(lightGray, pink)
                }
                else -> {
                    binding.linearLayout.setBackgroundColor(resources.getColor(R.color.pink))
                }
            }

           binding.chosenMoodImageBtn.setImageResource(R.drawable.good)

            color = "pink"
            mood = "good"
        }

        binding.greatImageBtn.setOnClickListener {

            when (color) {
                "pink" -> {
                    moodColorAnimation(pink, green)
                }
                "blue" -> {
                    moodColorAnimation(blue, green)
                }
                "dark_blue" -> {
                    moodColorAnimation(darkBlue, green)
                }
                "light_red" -> {
                    moodColorAnimation(lightRed, green)
                }
                "light_gray" -> {
                    moodColorAnimation(lightGray, green)
                }
                else -> {
                    binding.linearLayout.setBackgroundColor(resources.getColor(R.color.green))
                }
            }

            binding.chosenMoodImageBtn.setImageResource(R.drawable.great)

            color = "green"
            mood = "great"
        }

        binding.sadImageBtn.setOnClickListener {

            when (color) {
                "green" -> {
                    moodColorAnimation(green, blue)
                }
                "pink" -> {
                    moodColorAnimation(pink, blue)
                }
                "dark_blue" -> {
                    moodColorAnimation(darkBlue, blue)
                }
                "light_red" -> {
                    moodColorAnimation(lightRed, blue)
                }
                "light_gray" -> {
                    moodColorAnimation(lightGray, blue)
                }
                else -> {
                    binding.linearLayout.setBackgroundColor(resources.getColor(R.color.blue))
                }
            }

            binding.chosenMoodImageBtn.setImageResource(R.drawable.sad)

            color = "blue"
            mood = "sad"
        }

        binding.depressedImageBtn.setOnClickListener {

            when (color) {
                "green" -> {
                    moodColorAnimation(green, darkBlue)
                }
                "blue" -> {
                    moodColorAnimation(blue, darkBlue)
                }
                "pink" -> {
                    moodColorAnimation(pink, darkBlue)
                }
                "light_red" -> {
                    moodColorAnimation(lightRed, darkBlue)
                }
                "light_gray" -> {
                    moodColorAnimation(lightGray, darkBlue)
                }
                else -> {
                    binding.linearLayout.setBackgroundColor(resources.getColor(R.color.dark_blue))
                }
            }

            binding.chosenMoodImageBtn.setImageResource(R.drawable.depressed)

            color = "dark_blue"
            mood = "depressed"
        }

        binding.angryImageBtn.setOnClickListener {

            when (color) {
                "green" -> {
                    moodColorAnimation(green, lightRed)
                }
                "blue" -> {
                    moodColorAnimation(blue, lightRed)
                }
                "dark_blue" -> {
                    moodColorAnimation(darkBlue, lightRed)
                }
                "pink" -> {
                    moodColorAnimation(pink, lightRed)
                }
                "light_gray" -> {
                    moodColorAnimation(lightGray, lightRed)
                }
                else -> {
                    binding.linearLayout.setBackgroundColor(resources.getColor(R.color.light_red))
                }
            }

            binding.chosenMoodImageBtn.setImageResource(R.drawable.angry)

            color = "light_red"
            mood = "angry"
        }

        binding.neutralImageBtn.setOnClickListener {

            when (color) {
                "green" -> {
                    moodColorAnimation(green, lightGray)
                }
                "blue" -> {
                    moodColorAnimation(blue, lightGray)
                }
                "dark_blue" -> {
                    moodColorAnimation(darkBlue, lightGray)
                }
                "light_red" -> {
                    moodColorAnimation(lightRed, lightGray)
                }
                "pink" -> {
                    moodColorAnimation(pink, lightGray)
                }
                else -> {
                    binding.linearLayout.setBackgroundColor(resources.getColor(R.color.light_gray))
                }
            }

            binding.chosenMoodImageBtn.setImageResource(R.drawable.neutral)

            color = "light_gray"
            mood = "neutral"
        }

        binding.nextArrow.setOnClickListener {
            sharedPref.edit().putString("color", color).apply()
            sharedPref.edit().putString("mood", mood).apply()
            findNavController().navigate(R.id.action_moodFragment_to_moodDetailsFragment)
        }
    }

    private fun moodColorAnimation(startColor: Int, endColor: Int) {
        val moodColorAnimator = ObjectAnimator
            .ofInt(binding.linearLayout, "backgroundColor", startColor, endColor)
            .setDuration(1000)
        moodColorAnimator.setEvaluator(ArgbEvaluator())
        moodColorAnimator.start()
    }
}
