package com.tuwaiq.finalcapstone.presentation.moodDetailsFragment

import android.Manifest
import android.R.attr
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.anujkumarsharma.tooltipprogressbar.TooltipProgressBar
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.databinding.MoodDetailsFragmentBinding
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.presentation.memeApiFragment.CHOSE_MEME
import com.tuwaiq.finalcapstone.presentation.listFragment.TASK
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.endColor
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator


private const val TAG = "MoodDetailsFragment"
private const val REQUEST_IMAGE_CAPTURE = 1
var coord = 0.0005f
var c = false.toString()

@AndroidEntryPoint
class MoodDetailsFragment : Fragment() {

    private lateinit var binding: MoodDetailsFragmentBinding
    private val args: MoodDetailsFragmentArgs by navArgs()

    private val moodDetailsViewModel by lazy { ViewModelProvider(this).get(MoodDetailsViewModel::class.java) }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var layout: ConstraintLayout
    private lateinit var moodIv: ImageView
    private lateinit var picIv: ImageView
    private lateinit var noteEt: EditText
    private lateinit var addMoodButton: ImageButton
    private lateinit var takePicBtn: ImageButton
    private lateinit var memePickerIv: ImageView
    private lateinit var locationBtn: ImageButton
    private lateinit var latLangTv: TextView
    private var lat = 0.0
    private var long = 0.0
    private  var picUrl: String = ""
    private lateinit var color: String
    private lateinit var mood: String
    private lateinit var moodId: String
    private lateinit var meme: String
    private lateinit var userName: String
    private var storageRef = Firebase.storage.reference
    private lateinit var  fileProviderURI:Uri
    private lateinit var photoFile:File
    private lateinit var progressBar: TooltipProgressBar
    private lateinit var picSwitch: SwitchCompat
    private lateinit var noteTextView: TextView
    private lateinit var fab: FloatingActionButton
    private var colorRes = 0
    private lateinit var sharedPref: SharedPreferences

    private val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private var tempUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
            }
            }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mood_details_fragment, container, false)
        binding = MoodDetailsFragmentBinding.inflate(inflater, container, false)
        init(view)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val user1 = FirebaseAuth.getInstance().currentUser?.uid

        if (requestCode == REQUEST_IMAGE_CAPTURE ) {

            Log.e("formOnResult","is the photo file here ${photoFile.exists()}")

                val bitmapData = BitmapFactory.decodeFile(photoFile.absolutePath)
                binding.picImageView.setImageBitmap(bitmapData)
                binding.circularProgress.visibility = View.VISIBLE


            tempUri = context?.let {
                getImageUri(it.applicationContext, bitmapData)
            }

                try {
                    tempUri?.let { uri ->

                        val ref = storageRef.child("pics/$user1/${Calendar.getInstance().time}")
                        val pic = ref.putFile(uri)
                        pic.addOnProgressListener {
                            binding.circularProgress.setProgress(it.bytesTransferred.toDouble(), it.totalByteCount.toDouble())
                            val gradientType: Int = CircularProgressIndicator.LINEAR_GRADIENT
                            binding.circularProgress.setGradient(gradientType, resources.getColor(colorRes))
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
        //layout = view.findViewById(R.id.constraint_layout2)
        moodIv = view.findViewById(R.id.selected_mood_iv)
        noteEt = view.findViewById(R.id.note_et)
        addMoodButton = view.findViewById(R.id.add_mood)
        takePicBtn = view.findViewById(R.id.take_pic_btn)
        picIv = view.findViewById(R.id.pic_image_view)
        picSwitch = view.findViewById(R.id.pic_switch)
        memePickerIv = view.findViewById(R.id.meme_picker_iv)
        locationBtn = view.findViewById(R.id.location_btn)
        latLangTv = view.findViewById(R.id.lat_lang_tv)
        fab = activity?.findViewById(R.id.fab)!!

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mood = args.mood
//        color = args.color
        meme = args.meme
        Log.d(TAG, "meme: ${meme}")


        sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)!!
        color = sharedPref.getString("color", "").toString()
        mood = sharedPref.getString("mood", "").toString()
        moodId = sharedPref.getString("moodId", "").toString()
        Log.d(TAG, "color: $moodId")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override fun onStop() {
        super.onStop()
        fab.show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.hide()

        when (mood) {
            "good" -> {
                binding.selectedMoodIv.setImageResource(R.drawable.good)
            }
            "great" -> {
                binding.selectedMoodIv.setImageResource(R.drawable.great)
            }
            "sad" -> {
                binding.selectedMoodIv.setImageResource(R.drawable.sad)
            }
            "depressed" -> {
                binding.selectedMoodIv.setImageResource(R.drawable.depressed)
            }
            "angry" -> {
                binding.selectedMoodIv.setImageResource(R.drawable.angry)
            }
            "neutral" -> {
                binding.selectedMoodIv.setImageResource(R.drawable.neutral)
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
                binding.noteEt.setTextColor(resources.getColor(R.color.dark_pink))
                binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_pink))
                colorRes = R.color.dark_pink
            }
            "green" -> {
                binding.noteEt.setTextColor(resources.getColor(R.color.dark_green))
                binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_green))
                colorRes = R.color.green
            }
            "blue" -> {
                binding.noteEt.setTextColor(resources.getColor(R.color.dark_blue))
                binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_blue))
                colorRes = R.color.blue
            }
            "dark_blue" -> {
                binding.noteEt.setTextColor(resources.getColor(R.color.darkest_blue))
                binding.noteEt.setHintTextColor(resources.getColor(R.color.darkest_blue))
                colorRes = R.color.dark_blue
            }
            "light_red" -> {
                binding.noteEt.setTextColor(resources.getColor(R.color.red))
                binding.noteEt.setHintTextColor(resources.getColor(R.color.red))
                colorRes = R.color.red
            }
            "light_gray" -> {
                binding.noteEt.setTextColor(resources.getColor(R.color.gray))
                binding.noteEt.setHintTextColor(resources.getColor(R.color.gray))
                colorRes = R.color.gray
            }
            else -> {
                binding.noteEt.setTextColor(resources.getColor(R.color.black))
                binding.noteEt.setHintTextColor(resources.getColor(R.color.black))
                colorRes = R.color.black
            }
        }
    }

    private fun getPhotoFile(fileName: String): File {

        return File(requireContext().applicationContext.filesDir, fileName)
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser?.uid

        Log.d(TAG, "chose: $CHOSE_MEME")
        if (CHOSE_MEME) {
            Log.d(TAG, "meme: ${meme}")
            binding.memePickerIv.load(meme)
            CHOSE_MEME = false
        }

        binding.memePickerIv.setOnClickListener {
            findNavController().navigate(R.id.action_moodDetailsFragment_to_memeApiFragment)
        }
        binding.picImageView.setOnClickListener {
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
            }else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA)
            }
        }

        binding.locationBtn.setOnClickListener{
            getCurrentLocation()
        }

        if (TASK == "UPDATE") {

//            locationBtn.setOnClickListener{
//            getCurrentLocation()
//        }

            binding.circularProgress.visibility = View.INVISIBLE

            binding.picImageView.setOnClickListener {
                binding.picImageView.isClickable = false
                Snackbar.make(it, "You can't edit your pic", Snackbar.LENGTH_SHORT).show()
            }
            FirebaseUtils().fireStoreDatabase.collection("Mood")
                .document(moodId).get().addOnCompleteListener {

                    when (it.result.getString("mood")) {
                        "good" -> binding.selectedMoodIv.setImageResource(R.drawable.good)
                        "great" -> binding.selectedMoodIv.setImageResource(R.drawable.great)
                        "sad" -> binding.selectedMoodIv.setImageResource(R.drawable.sad)
                        "depressed" -> binding.selectedMoodIv.setImageResource(R.drawable.depressed)
                        "angry" -> binding.selectedMoodIv.setImageResource(R.drawable.angry)
                        "neutral" -> binding.selectedMoodIv.setImageResource(R.drawable.neutral)
                        else -> binding.selectedMoodIv.setImageBitmap(null)
                    }

                    when (it.result.getString("color")) {
                            "pink" -> {
                                binding.noteEt.setTextColor(resources.getColor(R.color.dark_pink))
                                binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_pink))
                                colorRes = R.color.dark_pink
                            }
                            "green" -> {
                                binding.noteEt.setTextColor(resources.getColor(R.color.dark_green))
                                binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_green))
                                colorRes = R.color.green
                            }
                            "blue" -> {
                                binding.noteEt.setTextColor(resources.getColor(R.color.dark_blue))
                                binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_blue))
                                colorRes = R.color.blue
                            }
                            "dark_blue" -> {
                                binding.noteEt.setTextColor(resources.getColor(R.color.darkest_blue))
                                binding.noteEt.setHintTextColor(resources.getColor(R.color.darkest_blue))
                                colorRes = R.color.dark_blue
                            }
                            "light_red" -> {
                                binding.noteEt.setTextColor(resources.getColor(R.color.red))
                                binding.noteEt.setHintTextColor(resources.getColor(R.color.red))
                                colorRes = R.color.red
                            }
                            "light_gray" -> {
                                binding.noteEt.setTextColor(resources.getColor(R.color.gray))
                                binding.noteEt.setHintTextColor(resources.getColor(R.color.gray))
                                colorRes = R.color.gray
                            }
                            else -> {
                                binding.noteEt.setTextColor(resources.getColor(R.color.black))
                                binding.noteEt.setHintTextColor(resources.getColor(R.color.black))
                                colorRes = R.color.black
                            }
                    }

                    binding.noteEt.setText(it.result.getString("note"))

                    Glide.with(requireContext())
                        .load(it.result.getString("pic"))
                        .into(binding.picImageView)

                    if (it.result.getString("pic") != null) {
                    picUrl = it.result.getString("pic")!!
                    }

                    binding.memePickerIv.load(it.result.getString("memePic"))

                    binding.memePickerIv.setOnClickListener {
                        findNavController().navigate(R.id.action_moodDetailsFragment_to_memeApiFragment)
                    }

                    binding.addMood.setOnClickListener {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        lifecycleScope.launch {
                            userName = moodDetailsViewModel.currentUserName().toString()
                        }.invokeOnCompletion {
                            Log.d(TAG, "mood: $mood")
                            val note = uid?.let { uid ->
                                Mood(
                                    binding.noteEt.text.toString(),
                                    color,
                                    picUrl,
                                    mood,
                                    uid,
                                    userName,
                                    meme,
                                    lat,
                                    long,
                                    moodId
                                )
                            }
                            FirebaseUtils().fireStoreDatabase.collection("Users")
                                .document(uid!!).update("note", FieldValue.arrayUnion(note))

                            if (note != null) {
                                FirebaseUtils().fireStoreDatabase.collection("Mood")
                                    .document(moodId).set(note)
                            }
                            findNavController().navigate(R.id.action_moodDetailsFragment_to_listFragment2)
                        }
                    }
                }
            TASK = ""
        }

        binding.picSwitch.setOnCheckedChangeListener { compoundButton, b ->
            c = b.toString()
            Log.d(TAG, c)
        }

        binding.addMood.setOnClickListener {
            lifecycleScope.launch {
                userName = moodDetailsViewModel.currentUserName().toString()
                Log.d(TAG, userName)
                Log.d(TAG, lat.toString())
            }.invokeOnCompletion {

                if (user != null) {

                    val b = mutableListOf<com.google.android.gms.maps.model.LatLng>()
                    FirebaseUtils().fireStoreDatabase.collection("Mood").get().addOnSuccessListener {
                        it.forEach {
                            if (it.getDouble("lat")!! == 0.0 && it.getDouble("long")!! == 0.0) {

                            } else {
                            b.add(com.google.android.gms.maps.model.LatLng(it.getDouble("lat")!!, it.getDouble("long")!!))
                        }
                            }
                        Log.d(TAG, "LIST: $b")
                        val n = com.google.android.gms.maps.model.LatLng(lat, long)
                        Log.d(TAG, "ONE: $n")
                        if (b.contains(n)) {
                            Log.d(TAG, "onStart: yesss")
                            lat+= coord
                            long+= coord
                            coord = coord.plus(0.0005f)
                        }else {

                        }.also {
                            val note = Mood(
                                binding.noteEt.text.toString(),
                                color,
                                picUrl,
                                mood,
                                user,
                                userName,
                                meme,
                                lat,
                                long
                            )
                            var spf = SimpleDateFormat("E LLL dd hh:mm:ss z yyyy")
                            val parsedDueDate = spf.parse(Date().toString())
                            spf = SimpleDateFormat("dd MMM yyyy")
                            val formattedDueDate = spf.format(parsedDueDate)

                            moodDetailsViewModel.addMood(note)
//                            val ref = FirebaseFirestore.getInstance().collection("Mood").document()
//                            note.moodId = ref.id
//                            note.privatePic = c
//                            ref.set(note)

                            Log.d(TAG, "onStart: $note")
                            moodDetailsViewModel.updateUserMood(note)
                        }
                    }
                }
                findNavController().navigate(R.id.action_moodDetailsFragment_to_listFragment2)
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.lastLocation.addOnSuccessListener {
                binding.latLangTv.text = "${it.latitude}  ${it.longitude}"
                lat = it.latitude
                long = it.longitude
            }
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}