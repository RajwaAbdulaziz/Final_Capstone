package com.tuwaiq.finalcapstone.presentation.moodDetailsFragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue

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
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.DialogInterface
import android.location.Location
import android.os.Looper
import android.view.animation.AnticipateInterpolator
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.google.android.gms.location.*
import com.tuwaiq.finalcapstone.MyCallback
import java.util.concurrent.TimeUnit


private const val TAG = "MoodDetailsFragment"
private const val REQUEST_IMAGE_CAPTURE = 1
var coord = 0.0005f
var c = false.toString()
var progress = 0.0
var progressComplete = 0.0
var hasPic = false
var hasLocation = false
@AndroidEntryPoint
class MoodDetailsFragment : Fragment() {

    private lateinit var binding: MoodDetailsFragmentBinding
    private val args: MoodDetailsFragmentArgs by navArgs()

    private val moodDetailsViewModel by lazy { ViewModelProvider(this).get(MoodDetailsViewModel::class.java) }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = 0.0
    private var long = 0.0
    private  var picUrl: String = ""
    private lateinit var color: String
    private lateinit var mood: String
    private lateinit var moodId: String
    private lateinit var meme: String
    private var storageRef = Firebase.storage.reference
    private lateinit var  fileProviderURI:Uri
    private lateinit var photoFile:File
    private lateinit var fab: FloatingActionButton
    private var colorRes = 0
    private lateinit var sharedPref: SharedPreferences

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var currentLocation: Location? = null
    private var userName = ""
    private var username = ""

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
    ): View {
        binding = MoodDetailsFragmentBinding.inflate(inflater, container, false)
        init()
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
                            hasPic = true
                            progress = it.bytesTransferred.toDouble()
                            progressComplete = it.totalByteCount.toDouble()
                            Log.d(TAG, "progressss: ${it.bytesTransferred.toDouble()}")
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

    private fun init() {
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

        val user = FirebaseUtils().auth.currentUser?.uid

        Log.d(TAG, "chose: $CHOSE_MEME")
        if (CHOSE_MEME) {
            Log.d(TAG, "meme: $meme")
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
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }

        binding.locationBtn.setOnClickListener {
            hasLocation = true
            getCurrentLocation()
        }

//        if (TASK == "UPDATE") {
//
////            locationBtn.setOnClickListener{
////            getCurrentLocation()
////        }
//
//            binding.circularProgress.visibility = View.INVISIBLE
//
//            binding.picImageView.setOnClickListener {
//                binding.picImageView.isClickable = false
//                Snackbar.make(it, "You can't edit your pic", Snackbar.LENGTH_SHORT).show()
//            }
//            FirebaseUtils().fireStoreDatabase.collection("Mood")
//                .document(moodId).get().addOnCompleteListener {
//
//                    when (it.result.getString("mood")) {
//                        "good" -> binding.selectedMoodIv.setImageResource(R.drawable.good)
//                        "great" -> binding.selectedMoodIv.setImageResource(R.drawable.great)
//                        "sad" -> binding.selectedMoodIv.setImageResource(R.drawable.sad)
//                        "depressed" -> binding.selectedMoodIv.setImageResource(R.drawable.depressed)
//                        "angry" -> binding.selectedMoodIv.setImageResource(R.drawable.angry)
//                        "neutral" -> binding.selectedMoodIv.setImageResource(R.drawable.neutral)
//                        else -> binding.selectedMoodIv.setImageBitmap(null)
//                    }
//
//                    when (it.result.getString("color")) {
//                        "pink" -> {
//                            binding.noteEt.setTextColor(resources.getColor(R.color.dark_pink))
//                            binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_pink))
//                            colorRes = R.color.dark_pink
//                        }
//                        "green" -> {
//                            binding.noteEt.setTextColor(resources.getColor(R.color.dark_green))
//                            binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_green))
//                            colorRes = R.color.green
//                        }
//                        "blue" -> {
//                            binding.noteEt.setTextColor(resources.getColor(R.color.dark_blue))
//                            binding.noteEt.setHintTextColor(resources.getColor(R.color.dark_blue))
//                            colorRes = R.color.blue
//                        }
//                        "dark_blue" -> {
//                            binding.noteEt.setTextColor(resources.getColor(R.color.darkest_blue))
//                            binding.noteEt.setHintTextColor(resources.getColor(R.color.darkest_blue))
//                            colorRes = R.color.dark_blue
//                        }
//                        "light_red" -> {
//                            binding.noteEt.setTextColor(resources.getColor(R.color.red))
//                            binding.noteEt.setHintTextColor(resources.getColor(R.color.red))
//                            colorRes = R.color.red
//                        }
//                        "light_gray" -> {
//                            binding.noteEt.setTextColor(resources.getColor(R.color.gray))
//                            binding.noteEt.setHintTextColor(resources.getColor(R.color.gray))
//                            colorRes = R.color.gray
//                        }
//                        else -> {
//                            binding.noteEt.setTextColor(resources.getColor(R.color.black))
//                            binding.noteEt.setHintTextColor(resources.getColor(R.color.black))
//                            colorRes = R.color.black
//                        }
//                    }
//
//                    binding.noteEt.setText(it.result.getString("note"))
//
//                    Glide.with(requireContext())
//                        .load(it.result.getString("pic"))
//                        .into(binding.picImageView)
//
//                    if (it.result.getString("pic") != null) {
//                        picUrl = it.result.getString("pic")!!
//                    }
//
//                    binding.memePickerIv.load(it.result.getString("memePic"))
//
//                    binding.memePickerIv.setOnClickListener {
//                        findNavController().navigate(R.id.action_moodDetailsFragment_to_memeApiFragment)
//                    }
//
//                    binding.addMood.setOnClickListener {
//                        val uid = FirebaseAuth.getInstance().currentUser?.uid
//
//                        moodDetailsViewModel.currentUserName(object : MyCallback {
//                            override fun username(name: String) {
//                                super.username(name)
//
//                                userName = name
//                            }
//
//                        }).toString()
//
//                        Log.d(TAG, "mood: $mood")
//                        val note = uid?.let { uid ->
//                            Mood(
//                                binding.noteEt.text.toString(),
//                                color,
//                                picUrl,
//                                mood,
//                                uid,
//                                userName,
//                                meme,
//                                lat,
//                                long,
//                                moodId
//                            )
//                        }
//                        FirebaseUtils().fireStoreDatabase.collection("Users")
//                            .document(uid!!).update("note", FieldValue.arrayUnion(note))
//
//                        if (note != null) {
//                            FirebaseUtils().fireStoreDatabase.collection("Mood")
//                                .document(moodId).set(note)
//                        }
//                        findNavController().navigate(R.id.action_moodDetailsFragment_to_listFragment2)
//
//                    }
//                }
//            TASK = ""
//        }

        binding.picSwitch.setOnCheckedChangeListener { _, b ->
            c = b.toString()
            Log.d(TAG, c)
        }

        binding.addMood.setOnClickListener {

            moodDetailsViewModel.currentUserName(object : MyCallback {
                override fun username(name: String) {
                    super.username(name)

                    username = name
                }
            })

                if (user != null) {

                    val b = mutableListOf<com.google.android.gms.maps.model.LatLng>()
                    FirebaseUtils().fireStoreDatabase.collection("Mood").get()
                        .addOnSuccessListener {
                            it.forEach {
                                if (it.getDouble("lat")!! == 0.0 && it.getDouble("long")!! == 0.0) {

                                } else {
                                    b.add(
                                        com.google.android.gms.maps.model.LatLng(
                                            it.getDouble("lat")!!,
                                            it.getDouble("long")!!
                                        )
                                    )
                                }
                            }
                            Log.d(TAG, "LIST: $b")
                            val n = com.google.android.gms.maps.model.LatLng(lat, long)
                            Log.d(TAG, "ONE: $n")
                            if (b.contains(n)) {
                                Log.d(TAG, "onStart: yesss")
                                lat += coord
                                long += coord
                                coord = coord.plus(0.0005f)
                            } else {

                            }.also {
                                if (hasPic && progress != progressComplete) {
                                    AlertDialog.Builder(context)
                                        .setMessage(resources.getText(R.string.img_not_uploaded))
                                        .setNegativeButton(
                                            resources.getText(R.string.fine),
                                            DialogInterface.OnClickListener { dialogInterface, i ->
                                                saveMood(user)
                                                findNavController().navigate(R.id.action_moodDetailsFragment_to_listFragment2)
                                            })
                                        .setPositiveButton(
                                            resources.getText(R.string.wait),
                                            DialogInterface.OnClickListener { dialogInterface, i ->
                                                dialogInterface.dismiss()
                                            })
                                        .create()
                                        .show()
                                } else {
                                    saveMood(user)
                                    findNavController().navigate(R.id.action_moodDetailsFragment_to_listFragment2)
                                }
                            }
                        }
                }
        }
    }

    private fun saveMood(user: String?) {
        val note = Mood(
            binding.noteEt.text.toString(),
            color,
            picUrl,
            mood,
            user,
            username,
            meme,
            lat,
            long
        )
        moodDetailsViewModel.addMood(note)
        moodDetailsViewModel.updateUserMood(note)
    }

    private fun getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation: ")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            // TODO: Step 1.3, Create a LocationRequest.
            locationRequest = LocationRequest.create().apply {
                interval = TimeUnit.SECONDS.toMillis(60)
                fastestInterval = TimeUnit.SECONDS.toMillis(30)
                maxWaitTime = TimeUnit.MINUTES.toMillis(2)
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)

                    currentLocation = locationResult.lastLocation

                    binding.latLangTv.text = "${currentLocation!!.latitude}  ${currentLocation!!.longitude}"
                    lat = currentLocation!!.latitude
                    long = currentLocation!!.longitude

                    Log.d(TAG, "currentLocation $currentLocation")
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//            fusedLocationClient.lastLocation.addOnSuccessListener {
//                binding.latLangTv.text = "${it.latitude}  ${it.longitude}"
//                lat = it.latitude
//                long = it.longitude
//            }
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

    override fun onPause() {
        if (hasLocation) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
        super.onPause()
    }
}