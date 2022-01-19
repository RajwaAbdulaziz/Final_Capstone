package com.tuwaiq.finalcapstone.presentation.mapViewFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*

private const val TAG = "MapViewFragment"
var COORDINATE_OFFSET = 0.90000f
class MapViewFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var map: MapView
    private var moodPic = 0
    private var lat = 0.0
    private var long = 0.0
    private var mapList = mutableListOf<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.map_view_fragment, container, false)
        map = view.findViewById(R.id.map) as MapView
        map.onCreate(savedInstanceState)
        map.onResume()
        map.getMapAsync(this)
        return view
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.let {
            googleMap = it
        }
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )

        FirebaseUtils().firestoreDatabase.collection("Mood").get().addOnSuccessListener {
            it.forEach {
                CoroutineScope(Dispatchers.Main).launch {
                    val b =
                        FirebaseUtils().firestoreDatabase.collection("Mood").document(it.id).get()
                            .await()

                    var smallDot = BitmapFactory.decodeResource(
                        requireContext().resources,
                        R.drawable.good
                    )

                    when (b.getString("mood")) {
                        "good" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                R.drawable.good
                            )
                        }
                        "great" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                R.drawable.great
                            )
                        }
                        "sad" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                R.drawable.sad
                            )
                        }
                        "depressed" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                R.drawable.depressed
                            )
                        }
                        "angry" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                R.drawable.angry
                            )
                        }
                        "neutral" -> {
                            smallDot = BitmapFactory.decodeResource(
                                requireContext().resources,
                                R.drawable.neutral
                            )
                        }
                    }

                    val resizedBitmap = Bitmap.createScaledBitmap(smallDot, 80, 80, false)

                    val new = BitmapDescriptorFactory.fromBitmap(resizedBitmap)

                    if (b.getDouble("lat") == 0.0 && b.getDouble("long") == 0.0) {

                    } else {
//                        mapList.add(LatLng(b.getDouble("lat")!!, b.getDouble("long")!!))
//                        if (mapList.size == 1) {
//                            //Log.d(TAG, "one")
//                            b.getDouble("lat")?.let { it1 ->
//                                LatLng(it1, b.getDouble("long")!!)
//                            }
//                                ?.let { it2 ->
//                                    MarkerOptions()
//                                        .position(it2)
//                                        .icon(new)
//                                }?.let { it3 ->
//                                    googleMap.addMarker(
//                                        it3
//
//                                    )
//                                }
//                        } else {
//                            Log.d(TAG, "list: $mapList")
//                            if (mapList.contains(
//                                    LatLng(
//                                        b.getDouble("lat")!!,
//                                        b.getDouble("long")!!
//                                    )
//                                )
//                            ) {
//
//                                COORDINATE_OFFSET+= 0.50000f
////                                c.update("lat", b.getDouble("lat")!!.plus(COORDINATE_OFFSET))
////                                c.update("long", b.getDouble("long")!!.plus(COORDINATE_OFFSET))
//
//    val d = 0

                        val a = MarkerOptions()
                            .position(
                                LatLng(
                                    b.getDouble("lat")!!,
                                    b.getDouble("long")!!
                                )
                            )
                            .icon(new)
                        Log.d(TAG, "marker: ${a.position}")
                        googleMap.addMarker(a)
                        //delay(3000)
//                                b.getDouble("lat")?.let { it1 ->
//                                    LatLng(it1, b.getDouble("long")!!)
//                                }
//                                    ?.let { it2 ->
//                                        MarkerOptions()
//                                            .position(it2.also {
//                                                it.latitude.plus(COORDINATE_OFFSET)
//                                                it.longitude.plus(COORDINATE_OFFSET)
//                                            })
//                                            .icon(new)
//                                    }?.let { it3 ->
//                                        googleMap.addMarker(
//                                            it3
//                                        )
//                                        Log.d(TAG, "marker: ${it3.position}")
//                                    }
//                            }
//                        }
                    }
                    Log.d(TAG, "id: ${it.id}")
                   if (it.getString("owner") == FirebaseUtils().auth.currentUser?.uid) {
                       googleMap.moveCamera(
                           CameraUpdateFactory.newLatLngZoom(
                               LatLng(
                                   b.getDouble("lat")!!,
                                   b.getDouble("long")!!,
                               ), 23.0f
                           )
                       )
                   }
                        //googleMap.setMaxZoomPreference(10f)
                   // }
                }
                    }
                }
            }
        }
