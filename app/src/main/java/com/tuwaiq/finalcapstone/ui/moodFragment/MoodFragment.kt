package com.tuwaiq.finalcapstone.ui.moodFragment

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.tuwaiq.finalcapstone.R

private const val TAG = "MoodFragment"
private var color = ""
private var mood = ""
class MoodFragment : Fragment() {

//    lateinit var recyclerView: RecyclerView
    private lateinit var layout: ConstraintLayout
    private lateinit var goodImageBtn: ImageButton
    private lateinit var greatImageBtn: ImageButton
    private lateinit var sadImageBtn: ImageButton
    private lateinit var depressedImageBtn: ImageButton
    private lateinit var angryImageBtn: ImageButton
    private lateinit var huhImageBtn: ImageButton
    private lateinit var wtfImageBtn: ImageButton
    private lateinit var disgustedImageBtn: ImageButton
    private lateinit var nextArrow: ImageButton

    private val yellow: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.yellow)
    }
    private val yellowish: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.yellowish)
    }
    private val blue: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.blue)
    }
    private val darkBlue: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.dark_blue)
    }
    private val red: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.red)
    }
    private val pink: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.pink)
    }
    private val orange: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.orange)
    }
    private val green: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.green)
    }
    private val purple: Int by lazy {
        ContextCompat.getColor(requireContext(), R.color.purple)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mood, container, false)
        init(view)
//        val images = listOf(
//            Mood(R.drawable.happy),
//            Mood(R.drawable.sad),
//            Mood(R.drawable.angry)
//        )
//        recyclerView = view.findViewById(R.id.moods_recycler_view)
//        val m = LinearLayoutManager(context)
//        m.orientation = LinearLayoutManager.HORIZONTAL
//        recyclerView.layoutManager = m
//        recyclerView.adapter = MoodAdapter(images)
        return view
    }

    private fun init(view: View) {
        layout = view.findViewById(R.id.constraint_layout)
        goodImageBtn = view.findViewById(R.id.good_image_btn)
        greatImageBtn = view.findViewById(R.id.great_image_btn)
        sadImageBtn = view.findViewById(R.id.sad_image_btn)
//        depressedImageBtn = view.findViewById(R.id.depressed_image_btn)
        angryImageBtn = view.findViewById(R.id.angry_image_btn)
//        huhImageBtn = view.findViewById(R.id.huh_image_btn)
//        wtfImageBtn = view.findViewById(R.id.wtf_image_btn)
//        disgustedImageBtn = view.findViewById(R.id.disgusted_image_btn)
        nextArrow = view.findViewById(R.id.next_arrow)
    }

    override fun onStart() {
        super.onStart()

       goodImageBtn.setOnClickListener {
            when (color) {
//                "yellowish" -> {
//                    moodColorAnimation(yellowish, yellow)
//                }
//                "blue" -> {
//                    moodColorAnimation(blue, yellow)
//                }
//                "dark_blue" -> {
//                    moodColorAnimation(darkBlue, yellow)
//                }
//                "red" -> {
//                    moodColorAnimation(red, yellow)
//                }
                "purple" -> {
                    moodColorAnimation(purple, pink)
                }
                "orange" -> {
                    moodColorAnimation(orange, pink)
                }
                "green" -> {
                    moodColorAnimation(green, pink)
                }
                else -> {
                    layout.setBackgroundColor(resources.getColor(R.color.pink))
                }
            }
            color = "pink"
            mood = "good"
        }

        greatImageBtn.setOnClickListener {
            when (color) {
//                "yellow" -> {
//                    moodColorAnimation(yellow, yellowish)
//                }
//                "blue" -> {
//                    moodColorAnimation(blue, yellowish)
//                }
//                "dark_blue" -> {
//                    moodColorAnimation(darkBlue, yellowish)
//                }
//                "red" -> {
//                    moodColorAnimation(red, yellowish)
//                }
                "pink" -> {
                    moodColorAnimation(pink, green)
                }
                "orange" -> {
                    moodColorAnimation(orange, green)
                }
                "purple" -> {
                    moodColorAnimation(purple, green)
                }
                else -> {
                    layout.setBackgroundColor(resources.getColor(R.color.green))
                }
            }
            color = "green"
            mood = "great"
        }

        sadImageBtn.setOnClickListener {
            when (color) {
//                "yellowish" -> {
//                    moodColorAnimation(yellowish, blue)
//                }
//                "yellow" -> {
//                    moodColorAnimation(yellow, blue)
//                }
//                "dark_blue" -> {
//                    moodColorAnimation(darkBlue, blue)
//                }
//                "red" -> {
//                    moodColorAnimation(red, blue)
//                }
                "pink" -> {
                    moodColorAnimation(pink, purple)
                }
                "orange" -> {
                    moodColorAnimation(orange, purple)
                }
                "green" -> {
                    moodColorAnimation(green, purple)
                }
                else -> {
                    layout.setBackgroundColor(resources.getColor(R.color.purple))
                }
            }
            color = "purple"
            mood = "sad"
        }
//
//        depressedImageBtn.setOnClickListener {
//            when (color) {
//                "yellowish" -> {
//                    moodColorAnimation(yellowish, darkBlue)
//                }
//                "blue" -> {
//                    moodColorAnimation(blue, darkBlue)
//                }
//                "yellow" -> {
//                    moodColorAnimation(yellow, darkBlue)
//                }
//                "red" -> {
//                    moodColorAnimation(red, darkBlue)
//                }
//                "pink" -> {
//                    moodColorAnimation(pink, darkBlue)
//                }
//                "orange" -> {
//                    moodColorAnimation(orange, darkBlue)
//                }
//                "green" -> {
//                    moodColorAnimation(green, darkBlue)
//                }
//                else -> {
//                    layout.setBackgroundColor(resources.getColor(R.color.dark_blue))
//                }
//            }
//            color = "dark_blue"
//            mood = "depressed"
//        }
//
        angryImageBtn.setOnClickListener {
            when (color) {
//                "yellowish" -> {
//                    moodColorAnimation(yellowish, red)
//                }
//                "blue" -> {
//                    moodColorAnimation(blue, red)
//                }
//                "dark_blue" -> {
//                    moodColorAnimation(darkBlue, red)
//                }
//                "yellow" -> {
//                    moodColorAnimation(yellow, red)
//                }
                "pink" -> {
                    moodColorAnimation(pink, orange)
                }
                "purple" -> {
                    moodColorAnimation(purple, orange)
                }
                "green" -> {
                    moodColorAnimation(green, orange)
                }
                else -> {
                    layout.setBackgroundColor(resources.getColor(R.color.orange))
                }
            }
            color = "orange"
            mood = "angry"
        }
//
//        huhImageBtn.setOnClickListener {
//            when (color) {
//                "yellowish" -> {
//                    moodColorAnimation(yellowish, pink)
//                }
//                "blue" -> {
//                    moodColorAnimation(blue, pink)
//                }
//                "dark_blue" -> {
//                    moodColorAnimation(darkBlue, pink)
//                }
//                "red" -> {
//                    moodColorAnimation(red, pink)
//                }
//                "yellow" -> {
//                    moodColorAnimation(yellow, pink)
//                }
//                "orange" -> {
//                    moodColorAnimation(orange, pink)
//                }
//                "green" -> {
//                    moodColorAnimation(green, pink)
//                }
//                else -> {
//                    layout.setBackgroundColor(resources.getColor(R.color.pink))
//                }
//            }
//            color = "pink"
//            mood = "huh"
//        }
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
            val action = MoodFragmentDirections.actionMoodFragmentToMoodDetailsFragment(color, mood)
            findNavController().navigate(action)
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
