package com.tuwaiq.finalcapstone.ui.mapViewFragment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.Mood
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



import android.graphics.Bitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.BitmapFactory
import com.google.common.reflect.Reflection.getPackageName






private const val TAG = "MapViewFragment"
class MapViewFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var map: MapView
    private var moodPic = 0
    private var lat = 0.0
    private var long = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.tuwaiq.finalcapstone.R.layout.map_view_fragment, container, false)
        map = view.findViewById(com.tuwaiq.finalcapstone.R.id.map) as MapView
        map.onCreate(savedInstanceState)
        map.onResume()
        map.getMapAsync(this)
        return view
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.let {
            googleMap = it
        }
        FirebaseUtils().firestoreDatabase.collection("Mood").get().addOnSuccessListener {
            it.forEach {
                CoroutineScope(Dispatchers.Main).launch {
                    val b = FirebaseUtils().firestoreDatabase.collection("Mood").document(it.id).get().await()
//                    lat = b.getDouble("lat")!!
//                    long = b.getDouble("long")!!
//                    Log.d(TAG, "lat: $lat long: $long")

                    var smallDot = BitmapFactory.decodeResource(
                        requireContext().resources,
                        com.tuwaiq.finalcapstone.R.drawable.good
                    )



                    when(b.getString("mood")) {
                        "good" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                com.tuwaiq.finalcapstone.R.drawable.good
                            )
                        }
                        "great" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                com.tuwaiq.finalcapstone.R.drawable.great
                            )
                        }
                        "sad" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                com.tuwaiq.finalcapstone.R.drawable.sad
                            )
                        }
                        "depressed" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                com.tuwaiq.finalcapstone.R.drawable.depressed
                            )
                        }
                        "angry" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                com.tuwaiq.finalcapstone.R.drawable.angry
                            )
                        }
                        "neutral" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                com.tuwaiq.finalcapstone.R.drawable.neutral
                            )
                        }
                    }

                    val resizedBitmap = Bitmap.createScaledBitmap(smallDot, 80, 80, false)

                    val new = BitmapDescriptorFactory.fromBitmap(resizedBitmap)

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(b.getDouble("lat")!!, b.getDouble("long")!!))
                            .icon(new)

                    )
                    Log.d(TAG, "${b.getDouble("lat")!!} ${b.getDouble("long")!!}")
                    }
                }
//                    .addOnSuccessListener {
//                    lat = it.getString("lat")?.toDouble() ?: 0.0
//                    long = it.getString("long")?.toDouble() ?: 0.0
//                }
            }





        googleMap.setMaxZoomPreference(200f)
    }
}
