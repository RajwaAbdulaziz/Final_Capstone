package com.tuwaiq.finalcapstone.ui.mapViewFragment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tuwaiq.finalcapstone.R

private const val LOCATION_PERMISSION_REQ_CODE = 1000
class MapViewFragment : Fragment() {

    private lateinit var viewModel: MapViewViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.map_view_fragment, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onStart() {
        super.onStart()
        button.setOnClickListener {
            getCurrentLocation()
        }
    }
    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            &&  ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
//                latitude = location.latitude
//                longitude = location.longitude
//                textView.text = latitude.toString()
//                textView2.text = longitude.toString()
                Toast.makeText(context, "${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
//                tvLatitude.text = "Latitude: ${location.latitude}"
//                tvLongitude.text = "Longitude: ${location.longitude}"
                //tvProvider.text = "Provider: ${location.provider}"
                //btOpenMap.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Toast.makeText(
                    context, "Failed on getting current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_REQ_CODE -> {
//                if (grantResults.isNotEmpty() &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED
//                ) {
//                    // permission granted
//                } else {
//                    // permission denied
//                    Toast.makeText(
//                        context, "You need to grant permission to access location",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }
}