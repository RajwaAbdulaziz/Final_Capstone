package com.tuwaiq.finalcapstone.ui.calenderFragment

import android.icu.util.LocaleData
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuwaiq.finalcapstone.R
import java.util.*

private const val TAG = "CalenderFragment"
class CalenderFragment : Fragment() {

    private lateinit var viewModel: CalenderViewModel
    private lateinit var calendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calender_fragment, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        return view
    }

    override fun onStart() {
        super.onStart()

        calendarView.maxDate = Calendar.getInstance().timeInMillis

        calendarView.setOnDateChangeListener { p0, p1, p2, p3 ->
            //findNavController().navigate(R.id.action_calenderFragment2_to_moodFragment)
        }
    }
}