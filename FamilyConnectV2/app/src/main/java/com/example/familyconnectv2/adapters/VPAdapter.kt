package com.example.familyconnectv2.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.NavController
import com.example.familyconnectv2.ui.agenda.AgendaFragment
import com.example.familyconnectv2.ui.agenda.AgendaViewModel
import com.example.familyconnectv2.ui.monthcalendar.MonthCalendarFragment
import com.example.familyconnectv2.ui.weekcalendar.WeekCalendarFragment

class VPAdapter (fragmentManager: FragmentManager,private val navController: NavController?) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return AgendaFragment()
            1 -> return WeekCalendarFragment()
            2 -> return MonthCalendarFragment()
            else -> return AgendaFragment() // Default, možete prilagoditi prema potrebi
        }
    }

    override fun getCount(): Int {
        // Broj tabova
        return 3
    }
}