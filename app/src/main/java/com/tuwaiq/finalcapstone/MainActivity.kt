package com.tuwaiq.finalcapstone

import android.graphics.drawable.ColorDrawable
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
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI

var bool = false
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var chipNav: ChipNavigationBar
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_nav)
        //chipNav = findViewById(R.id.chip_nav)
        fab = findViewById(R.id.fab)
        bottomBar = findViewById(R.id.bottom_app_bar)
        val navController = findNavController(R.id.fragmentContainerView)

//        chipNav.setOnItemSelectedListener {
//            bottomNav.selectedItemId = it
//        }

//
//        bottomBar.onItemSelectedListener = { view, menuItem ->
//            /**
//             * handle menu item clicks here,
//             * but clicks on already selected item will not affect this callback
//             */
//        }
//
//        bottomBar.onItemReselectedListener = { view, menuItem ->
//            /**
//             * handle here all the click in already selected items
//             */
//        }

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            //chipNav.setItemSelected(destination.id)
//        }

       // ExpandableBottomBarNavigationUI.setupWithNavController(bottomBar, navController)

        //bottomNav.setupWithNavController(navController)

        bottomNav.background = null
        bottomNav.setupWithNavController(navController)

        fab.setOnClickListener {
            bool = if (!bool) {
                navController.navigate(R.id.moodFragment)
                val interpolator = OvershootInterpolator()
                ViewCompat.animate(fab).rotation(135f).withLayer().setDuration(300)
                    .setInterpolator(interpolator).start()
                true
            } else {
                navController.navigate(R.id.listFragment2)
                val interpolator = OvershootInterpolator()
                ViewCompat.animate(fab).rotation(90f).withLayer().setDuration(300)
                    .setInterpolator(interpolator).start()
                false
            }
        }
    }
}