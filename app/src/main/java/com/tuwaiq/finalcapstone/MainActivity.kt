package com.tuwaiq.finalcapstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.ListFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuwaiq.finalcapstone.ui.calenderFragment.CalenderFragment
import com.tuwaiq.finalcapstone.ui.loginFragment.LoginFragment
import com.tuwaiq.finalcapstone.ui.mapViewFragment.MapViewFragment
import android.view.WindowManager

import android.os.Build
import android.view.Window
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.joaquimley.faboptions.FabOptions
import androidx.core.view.ViewCompat

import android.view.animation.OvershootInterpolator





class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var chipNav: ChipNavigationBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_nav)
        chipNav = findViewById(R.id.chip_nav)


        val navController = findNavController(R.id.fragmentContainerView)

        chipNav.setOnItemSelectedListener {
            bottomNav.selectedItemId = it
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            chipNav.setItemSelected(destination.id )}



        bottomNav.setupWithNavController(navController)

    }
}