package com.tuwaiq.finalcapstone.ui.moodDetailsFragment

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.ui.moodFragment.MoodFragmentDirections
import com.tuwaiq.finalcapstone.utils.FirebaseUtils

private const val TAG = "MoodDetailsFragment"
class MoodDetailsFragment : Fragment() {

    private val args: MoodDetailsFragmentArgs by navArgs()

    private lateinit var viewModel: MoodDetailsViewModel
    private lateinit var layout: ConstraintLayout
    private lateinit var moodIv: ImageView
    private lateinit var noteEt: EditText
    private lateinit var addMoodButton: ImageButton
    private lateinit var color: String
    private lateinit var mood: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mood_details_fragment, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        layout = view.findViewById(R.id.constraint_layout2)
        moodIv = view.findViewById(R.id.selected_mood_iv)
        noteEt = view.findViewById(R.id.note_et)
        addMoodButton = view.findViewById(R.id.add_mood)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mood = args.mood
        color = args.color
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (mood) {
            "good" -> {
                moodIv.setImageResource(R.drawable.good)
            }
            "great" -> {
                moodIv.setImageResource(R.drawable.great)
            }
            "sad" -> {
                moodIv.setImageResource(R.drawable.sad)
            }
//            "depressed" -> {
//                moodIv.setImageResource(R.drawable.depressed)
//            }
            "angry" -> {
                moodIv.setImageResource(R.drawable.angry)
            }
//            "huh" -> {
//                moodIv.setImageResource(R.drawable.huh)
//            }
//            "wtf" -> {
//                moodIv.setImageResource(R.drawable.wtf)
//            }
//            "disgusted" -> {
//                moodIv.setImageResource(R.drawable.disgusted)
//            }
        }

        when (color) {
//            "yellow" -> {
//                noteEt.setTextColor(resources.getColor(R.color.yellow))
//                noteEt.setHintTextColor(resources.getColor(R.color.yellow))
//            }
//            "yellowish" -> {
//                noteEt.setTextColor(resources.getColor(R.color.yellowish))
//                noteEt.setHintTextColor(resources.getColor(R.color.yellowish))
//            }
//            "blue" -> {
//                noteEt.setTextColor(resources.getColor(R.color.blue))
//                noteEt.setHintTextColor(resources.getColor(R.color.blue))
//            }
//            "dark_blue" -> {
//                noteEt.setTextColor(resources.getColor(R.color.dark_blue))
//                noteEt.setHintTextColor(resources.getColor(R.color.dark_blue))
//            }
            "purple" -> {
                noteEt.setTextColor(resources.getColor(R.color.purple))
                noteEt.setHintTextColor(resources.getColor(R.color.purple))
            }
            "pink" -> {
                noteEt.setTextColor(resources.getColor(R.color.pink))
                noteEt.setHintTextColor(resources.getColor(R.color.pink))
            }
            "orange" -> {
                noteEt.setTextColor(resources.getColor(R.color.orange))
                noteEt.setHintTextColor(resources.getColor(R.color.orange))
            }
            "green" -> {
                noteEt.setTextColor(resources.getColor(R.color.dark_green))
                noteEt.setHintTextColor(resources.getColor(R.color.dark_green))
            }
            else -> {
                noteEt.setTextColor(resources.getColor(R.color.black))
                noteEt.setHintTextColor(resources.getColor(R.color.black))
            }
        }
    }

    override fun onStart() {
        super.onStart()

        addMoodButton.setOnClickListener {
            val note = hashMapOf<String, Any>(
                "note" to noteEt.text.toString(),
                "color" to color
            )
            FirebaseUtils().firestoreDatabase.collection("notes")
                .add(note)
            val action = MoodDetailsFragmentDirections.actionMoodDetailsFragmentToListFragment2(color)
            findNavController().navigate(action)
        }
    }
}