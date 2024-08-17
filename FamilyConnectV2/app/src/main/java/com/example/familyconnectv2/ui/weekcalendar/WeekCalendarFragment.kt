package com.example.familyconnectv2.ui.weekcalendar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.ActivityAdapter
import com.example.familyconnectv2.adapters.WeekAdapter
import com.example.familyconnectv2.decorators.AnimationFacade
import com.example.familyconnectv2.decorators.WeeklyFacade
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.calendar.CalendarViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.DayOfWeek
import java.time.LocalDate
import androidx.lifecycle.Observer
import com.example.familyconnectv2.models.CalendarActivity
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class WeekCalendarFragment : Fragment() , WeekAdapter.OnItemListener {

    private lateinit var viewModel: WeekCalendarViewModel
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var backButton: Button
    private lateinit var forwardButton: Button

    private var currentWeekStartDate: LocalDate = LocalDate.now()
    private val weekCalendarFacade = WeeklyFacade()

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }

    private lateinit var addEventButton: FloatingActionButton
    private lateinit var addActivityButton: FloatingActionButton
    private lateinit var addGroupActivityButton: FloatingActionButton


    private lateinit var floatingActionButtonManager: AnimationFacade

    private lateinit var viewModelcalendarweek: CalendarViewModel
    private lateinit var viewModelcreateGroupweek: CreateGroupViewModel
    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var activitiesAdapter: ActivityAdapter
    private var allActivities: List<CalendarActivity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week_calendar, container, false)

        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)

        backButton = view.findViewById(R.id.backButtonweek)
        forwardButton = view.findViewById(R.id.forwardButton)

        backButton.setOnClickListener { previousWeekAction() }
        forwardButton.setOnClickListener { nextWeekAction()}

        addEventButton = view.findViewById(R.id.addbtnweek)
        addActivityButton = view.findViewById(R.id.addactivityweek)
        addGroupActivityButton = view.findViewById(R.id.addgroupweek)

        floatingActionButtonManager = AnimationFacade(
            addEventButton,
            addActivityButton,
            addGroupActivityButton,
            rotateOpen,
            rotateClose,
            fromBottom,
            toBottom
        )


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeekCalendarViewModel::class.java)
        initViews()
        setWeekView(currentWeekStartDate)

        val recyclerView: RecyclerView = view.findViewById(R.id.calendarRecyclerViewActivity)

        activitiesAdapter = ActivityAdapter(emptyList()) // Prvo inicijalno prazna lista
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = activitiesAdapter
        }

        viewModelcalendarweek = ViewModelProvider(this).get(CalendarViewModel::class.java)
        viewModelcalendarweek.initTokenManager(requireContext())

        viewModelcreateGroupweek = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        viewModelcreateGroupweek.initTokenManager(requireContext())

        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            viewModelcreateGroupweek.getUserIdByEmail(email)

        })

        viewModelcreateGroupweek.userId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != null) {
                viewModelcalendarweek.getActivitiesForDate(userId)
            }
        })

        viewModelcalendarweek.activities.observe(viewLifecycleOwner, Observer { activities ->
            activities?.let {
                // Ažuriranje podataka u adapteru
                allActivities = it.activities
                activitiesAdapter.setData(it.activities)
            }
        })

    }

    private fun initViews() {
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
    }

    private fun setWeekView(startOfWeek: LocalDate) {

        val dayOfWeekLabels = weekCalendarFacade.getDayOfWeekLabels()
        for (i in 0 until 7) {
            val textViewId = resources.getIdentifier("dayLabel$i", "id", requireContext().packageName)
            view?.findViewById<TextView>(textViewId)?.text = dayOfWeekLabels[i]
        }


        val formattedMonthYear = weekCalendarFacade.getFormattedMonthYear(startOfWeek)
        monthYearText.text = formattedMonthYear


        val daysInWeek = weekCalendarFacade.getDaysInWeek(startOfWeek)


        val calendarAdapter = WeekAdapter(daysInWeek, this)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun previousWeekAction() {
        currentWeekStartDate = weekCalendarFacade.getPreviousWeekStartDate(currentWeekStartDate)
        setWeekView(currentWeekStartDate)
    }

    private fun nextWeekAction() {
        currentWeekStartDate = weekCalendarFacade.getNextWeekStartDate(currentWeekStartDate)
        setWeekView(currentWeekStartDate)
    }

    private fun updateAdapterForSelectedDate(selectedDate: LocalDate) {
        val filteredActivities = allActivities.filter { activity ->
            val activityDate = activity.activity.date?.let {
                try {
                    // Ukloni vreme i vremensku zonu, zatim parsiraj
                    LocalDate.parse(it.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE)
                } catch (e: DateTimeParseException) {
                    null
                }
            }
            activityDate == selectedDate
        }
        activitiesAdapter.setData(filteredActivities)
    }

    override fun onItemClick(position: Int, date: LocalDate) {
        updateAdapterForSelectedDate(date)
    }

}