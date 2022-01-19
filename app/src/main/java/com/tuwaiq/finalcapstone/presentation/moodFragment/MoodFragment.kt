package com.tuwaiq.finalcapstone.presentation.moodFragment

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tuwaiq.finalcapstone.R
import kotlinx.coroutines.launch

private const val TAG = "MoodFragment"
private var color = ""
private var mood = ""
class MoodFragment : Fragment() {

    private val moodFragmentViewModel by lazy { ViewModelProvider(this).get(MoodFragmentViewModel::class.java) }
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
        init(view)
        return view
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

        lifecycleScope.launch {
            val userName = moodFragmentViewModel.userName()
            greetingsTv.text = resources.getString(R.string.greetings, userName)
        }

       goodImageBtn.setOnClickListener {

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
                    layout.setBackgroundColor(resources.getColor(R.color.pink))
                }
            }

           chosenMoodImageView.setImageResource(R.drawable.good)

            color = "pink"
            mood = "good"
        }

        greatImageBtn.setOnClickListener {

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
                    layout.setBackgroundColor(resources.getColor(R.color.green))
                }
            }

            chosenMoodImageView.setImageResource(R.drawable.great)

            color = "green"
            mood = "great"
        }

        sadImageBtn.setOnClickListener {

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
                    layout.setBackgroundColor(resources.getColor(R.color.blue))
                }
            }

            chosenMoodImageView.setImageResource(R.drawable.sad)

            color = "blue"
            mood = "sad"
        }

        depressedImageBtn.setOnClickListener {

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
                    layout.setBackgroundColor(resources.getColor(R.color.dark_blue))
                }
            }

            chosenMoodImageView.setImageResource(R.drawable.depressed)

            color = "dark_blue"
            mood = "depressed"
        }

        angryImageBtn.setOnClickListener {

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
                    layout.setBackgroundColor(resources.getColor(R.color.light_red))
                }
            }

            chosenMoodImageView.setImageResource(R.drawable.angry)

            color = "light_red"
            mood = "angry"
        }

        neutralImageButton.setOnClickListener {

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
                    layout.setBackgroundColor(resources.getColor(R.color.light_gray))
                }
            }

            chosenMoodImageView.setImageResource(R.drawable.neutral)

            color = "light_gray"
            mood = "neutral"
        }
//
//        wtfImageBtn.setOnClickListener {
//            when (color) {
//                "yellowish" -> {
//                    moodColorAnimation(yellowish, orange)
//                }
//                "blue" -> {
//                    moodColorAnimation(blue, orange)
//                }
//                "dark_blue" -> {
//                    moodColorAnimation(darkBlue, orange)
//                }
//                "red" -> {
//                    moodColorAnimation(red, orange)
//                }
//                "pink" -> {
//                    moodColorAnimation(pink, orange)
//                }
//                "yellow" -> {
//                    moodColorAnimation(yellow, orange)
//                }
//                "green" -> {
//                    moodColorAnimation(green, orange)
//                }
//                else -> {
//                    layout.setBackgroundColor(resources.getColor(R.color.orange))
//                }
//            }
//            color = "orange"
//            mood = "wtf"
//        }
//
//        disgustedImageBtn.setOnClickListener {
//            when (color) {
//                "yellowish" -> {
//                    moodColorAnimation(yellowish, green)
//                }
//                "blue" -> {
//                    moodColorAnimation(blue, green)
//                }
//                "dark_blue" -> {
//                    moodColorAnimation(darkBlue, green)
//                }
//                "red" -> {
//                    moodColorAnimation(red, green)
//                }
//                "pink" -> {
//                    moodColorAnimation(pink, green)
//                }
//                "orange" -> {
//                    moodColorAnimation(orange, green)
//                }
//                "yellow" -> {
//                    moodColorAnimation(yellow, green)
//                }
//                else -> {
//                    layout.setBackgroundColor(resources.getColor(R.color.green))
//                }
//            }
//            color = "green"
//            mood = "disgusted"
//        }



        nextArrow.setOnClickListener {
            sharedPref.edit().putString("color", color).apply()
            sharedPref.edit().putString("mood", mood).apply()
            findNavController().navigate(R.id.action_moodFragment_to_moodDetailsFragment)
        }
    }

    private fun moodColorAnimation(startColor: Int, endColor: Int) {
        val moodColorAnimator = ObjectAnimator
            .ofInt(layout, "backgroundColor", startColor, endColor)
            .setDuration(1000)
        moodColorAnimator.setEvaluator(ArgbEvaluator())
        moodColorAnimator.start()
    }
}
