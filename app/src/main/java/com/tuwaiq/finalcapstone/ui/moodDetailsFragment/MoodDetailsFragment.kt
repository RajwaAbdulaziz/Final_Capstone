package com.tuwaiq.finalcapstone.ui.moodDetailsFragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.ui.listFragment.TASK
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.net.URI
import java.util.*

private const val TAG = "MoodDetailsFragment"
private const val REQUEST_IMAGE_CAPTURE = 1
class MoodDetailsFragment : Fragment() {

    private val args: MoodDetailsFragmentArgs by navArgs()

    private val moodDetailsViewModel by lazy { ViewModelProvider(this).get(MoodDetailsViewModel::class.java) }

    private lateinit var viewModel: MoodDetailsViewModel
    private lateinit var layout: ConstraintLayout
    private lateinit var moodIv: ImageView
    private lateinit var picIv: ImageView
    private lateinit var noteEt: EditText
    private lateinit var addMoodButton: ImageButton
    private lateinit var takePicBtn: ImageButton
    private lateinit var picFile: File
    private  var picUrl: String = ""
    private lateinit var color: String
    private lateinit var mood: String
    private lateinit var moodId: String
    private lateinit var userName: String
    private var storageRef = Firebase.storage.reference
    private lateinit var  fileProviderURI:Uri
    private lateinit var photoFile:File
    private lateinit var progressBar: ProgressBar
    private var colorRes = 0

    private val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

   // private var picUri: Uri? = null
    private var tempUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mood_details_fragment, container, false)
        init(view)
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val user1 = FirebaseAuth.getInstance().currentUser?.uid

        if (requestCode == REQUEST_IMAGE_CAPTURE ) {

            Log.e("formOnResult","is the photo file here ${photoFile.exists()}")

                val bitmapData = BitmapFactory.decodeFile(photoFile.absolutePath)
                picIv.setImageBitmap(bitmapData)

            tempUri = context?.let {
                getImageUri(it.applicationContext, bitmapData)
            }

                try {
                    tempUri?.let { uri ->

                        val ref = storageRef.child("pics/$user1/${Calendar.getInstance().time}")
                        val pic = ref.putFile(uri)
                        pic.addOnProgressListener {
                            progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(colorRes))
                            progressBar.progress = ((100.0 * it.bytesTransferred) / it.totalByteCount).toInt()
                            Log.d(TAG, "onActivityResult: ${progressBar.progress}")
                        }

                        pic.continueWithTask{ task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val picUrll = it.result.toString()
                                    picUrl = picUrll
                                    Log.d(TAG, picUrl)
                                }
                            }.addOnFailureListener {
                                Log.e(TAG, "something" , it)
                            }
                    }
                } catch (e: Exception) {

                }
            }
           // picIv.setImageURI(tempUri)

            //picIv.setImageBitmap(piccUri)
//            data?.data.let {
//                picUri = it
//                picIv.setImageURI(it)
            //}

        }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        Log.d("getImageUri" , "bitmap $inImage")


        val path: String =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "IMG_" + Calendar.getInstance().getTime(), null)
        return Uri.parse(path)
    }

    private fun init(view: View) {
        layout = view.findViewById(R.id.constraint_layout2)
        moodIv = view.findViewById(R.id.selected_mood_iv)
        noteEt = view.findViewById(R.id.note_et)
        addMoodButton = view.findViewById(R.id.add_mood)
        takePicBtn = view.findViewById(R.id.take_pic_btn)
        picIv = view.findViewById(R.id.pic_image_view)
        progressBar = view.findViewById(R.id.progress_bar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mood = args.mood
        color = args.color
        moodId = args.moodId
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
            "depressed" -> {
                moodIv.setImageResource(R.drawable.depressed)
            }
            "angry" -> {
                moodIv.setImageResource(R.drawable.angry)
            }
            "neutral" -> {
                moodIv.setImageResource(R.drawable.neutral)
            }
//            "wtf" -> {
//                moodIv.setImageResource(R.drawable.wtf)
//            }
//            "disgusted" -> {
//                moodIv.setImageResource(R.drawable.disgusted)
//            }
        }

        when (color) {
            "pink" -> {
                noteEt.setTextColor(resources.getColor(R.color.dark_pink))
                noteEt.setHintTextColor(resources.getColor(R.color.dark_pink))
                colorRes = R.color.dark_pink
            }
            "green" -> {
                noteEt.setTextColor(resources.getColor(R.color.dark_green))
                noteEt.setHintTextColor(resources.getColor(R.color.dark_green))
                colorRes = R.color.green
            }
            "blue" -> {
                noteEt.setTextColor(resources.getColor(R.color.dark_blue))
                noteEt.setHintTextColor(resources.getColor(R.color.dark_blue))
                colorRes = R.color.blue
            }
            "dark_blue" -> {
                noteEt.setTextColor(resources.getColor(R.color.darkest_blue))
                noteEt.setHintTextColor(resources.getColor(R.color.darkest_blue))
                colorRes = R.color.dark_blue
            }
            "light_red" -> {
                noteEt.setTextColor(resources.getColor(R.color.red))
                noteEt.setHintTextColor(resources.getColor(R.color.red))
                colorRes = R.color.red
            }
            "light_gray" -> {
                noteEt.setTextColor(resources.getColor(R.color.gray))
                noteEt.setHintTextColor(resources.getColor(R.color.gray))
                colorRes = R.color.gray
            }
            else -> {
                noteEt.setTextColor(resources.getColor(R.color.black))
                noteEt.setHintTextColor(resources.getColor(R.color.black))
                colorRes = R.color.black
            }
        }


    }
    private fun getPhotoFile(fileName: String): File {

        val fileDir = File(requireContext().applicationContext.filesDir, fileName)

        return fileDir

    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser?.uid

        picIv.setOnClickListener {
            if (PackageManager.PERMISSION_GRANTED == context?.let {
                    ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA)
                }) {

                intent.also {
                    photoFile = getPhotoFile(Date().time.toString() + "photo.jpg")
                    fileProviderURI = FileProvider.getUriForFile(
                        requireContext(),
                        "com.tuwaiq.finalcapstone",
                        photoFile
                    )

                    it.putExtra(MediaStore.EXTRA_OUTPUT, fileProviderURI)
                    startActivityForResult(it, REQUEST_IMAGE_CAPTURE)

                    Log.d(TAG, "url: $picUrl")
                }
            }
        }

        if (TASK == "UPDATE") {
            FirebaseUtils().firestoreDatabase.collection("Mood")
                .document(moodId).get().addOnCompleteListener {

                    when (it.result.getString("mood")) {
                        "good" -> moodIv.setImageResource(R.drawable.good)
                        "great" -> moodIv.setImageResource(R.drawable.great)
                        "sad" -> moodIv.setImageResource(R.drawable.sad)
                        "depressed" -> moodIv.setImageResource(R.drawable.depressed)
                        "angry" -> moodIv.setImageResource(R.drawable.angry)
                        "neutral" -> moodIv.setImageResource(R.drawable.neutral)
                    }
                    noteEt.setText(it.result.getString("note"))
                    Glide.with(requireContext())
                        .load(it.result.getString("pic"))
                        .into(picIv)

                    picUrl = it.result.getString("pic")!!

                    addMoodButton.setOnClickListener {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        Log.d(TAG, "urlll $picUrl")
                        lifecycleScope.launch {
                            userName = moodDetailsViewModel.currentUserName().toString()
                            Log.d(TAG, userName)
                        }.invokeOnCompletion {
                            val note = Mood(
                                noteEt.text.toString(),
                                color,
                                picUrl,
                                mood,
                                uid,
                                userName,
                                moodId
                            )
                            Log.d(TAG, note.toString())
                            FirebaseUtils().firestoreDatabase.collection("Users")
                                .document(uid!!).update("note", FieldValue.arrayUnion(note))

                            FirebaseUtils().firestoreDatabase.collection("Mood")
                                .document(moodId).set(note)

                            val action =
                                MoodDetailsFragmentDirections.actionMoodDetailsFragmentToListFragment2(
                                    color
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            TASK = ""
        }


        addMoodButton.setOnClickListener {

            //val moodId = UUID.randomUUID().toString()
            lifecycleScope.launch {
                userName = moodDetailsViewModel.currentUserName().toString()
                Log.d(TAG, userName)
            }.invokeOnCompletion {

                val note = Mood(noteEt.text.toString(), color, picUrl, mood, user, userName)

//            val note = hashMapOf<String, Any?>(
//                "note" to noteEt.text.toString(),
//                "color" to color,
//                //"pic" to tempUri.toString()
//            )
                if (user != null) {
                    FirebaseUtils().firestoreDatabase.collection("Mood").add(note)
                    //val uid = FirebaseAuth.getInstance().currentUser?.uid
                    FirebaseUtils().firestoreDatabase.collection("Users")
                        .document(user).update("note", FieldValue.arrayUnion(note))
                }
               // Log.d(TAG, "note: $note")
                val action =
                    MoodDetailsFragmentDirections.actionMoodDetailsFragmentToListFragment2(color)
                findNavController().navigate(action)
            }
        }
    }
}
