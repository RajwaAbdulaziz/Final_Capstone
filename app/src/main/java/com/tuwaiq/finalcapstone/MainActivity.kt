package com.tuwaiq.finalcapstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import androidx.core.view.ViewCompat

import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.get
import com.google.android.material.bottomappbar.BottomAppBar
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.properties.Delegates

var bool = false
private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var chipNav: ChipNavigationBar
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomBar: BottomAppBar

    private var clicked = false
    private var changed = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_nav)
        fab = findViewById(R.id.fab)
        bottomBar = findViewById(R.id.bottom_app_bar)
        val navController = findNavController(R.id.fragmentContainerView)

        bottomNav.background = null
        bottomNav.setupWithNavController(navController)

        
        FirebaseUtils().fireStoreDatabase.collection("Chat").addSnapshotListener { value, error ->
            if (value?.isEmpty == false) {
                Log.d(TAG, "f: ${value.documents.last().getDate("time")}")
                Log.d(TAG, "s: ${value.documentChanges.last()?.document?.getDate("time")}")
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    if (destination.id != R.id.chatFragment) {

                        if (value.size() != value.documentChanges.size && value.documentChanges.isNotEmpty()) {

                                if (value.documents.last().getDate("time") != (value.documentChanges.last().document.getDate("time"))) {
                                    clicked = false
                                    if (!clicked) {
                                        bottomNav.getOrCreateBadge(R.id.chatFragment).backgroundColor =
                                            resources.getColor(R.color.dark_orange)
                                } else {
                                    clicked = true
                                    bottomNav.removeBadge(R.id.chatFragment)
                                }
                            } else {
                                bottomNav.removeBadge(R.id.chatFragment)
                            }
                        }
                    } else {
                        clicked = true
                        bottomNav.removeBadge(R.id.chatFragment)
                    }
                }
            }
        }

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

//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//
//            when (destination.id) {
//                R.id.loginFragment -> {
//                    bottomBar.visibility = View.INVISIBLE
//                    fab.visibility = View.INVISIBLE
//                }
//                R.id.registerFragment -> {
//                    bottomBar.visibility = View.INVISIBLE
//                    fab.visibility = View.INVISIBLE
//                }
//                else -> {
//                    bottomBar.visibility = View.VISIBLE
//                    fab.visibility = View.VISIBLE
//                }
//            }
//        }
        }
    }