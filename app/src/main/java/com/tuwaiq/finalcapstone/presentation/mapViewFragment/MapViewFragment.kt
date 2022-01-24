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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "MapViewFragment"
@AndroidEntryPoint
class MapViewFragment : Fragment(), OnMapReadyCallback {

    private val mapViewViewModel by viewModels<MapViewViewModel>()

    private lateinit var googleMap: GoogleMap
    private lateinit var map: MapView

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
        var smallDot = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.good
        )

        lifecycleScope.launch {
            mapViewViewModel.getLatLong(googleMap).onEach {
                it.forEach {
                    when (it.mood) {
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

                    if (it.latLng.latitude != 0.0 && it.latLng.longitude != 0.0) {
                        val d = MarkerOptions()
                            .position(
                                LatLng(
                                    it.latLng.latitude,
                                    it.latLng.longitude
                                )
                            )
                            .icon(new)

                        googleMap.addMarker(d)

                        if (it.owner == FirebaseUtils().auth.currentUser?.uid) {
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        it.latLng.latitude,
                                        it.latLng.longitude,
                                    ), 18.0f
                                )
                            )
                        }
                    }
                }
            }.launchIn(lifecycleScope)
        }
    }
}
