package com.example.familyconnectv2.ui.agenda

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.ActivityAdapter
import com.example.familyconnectv2.decorators.AnimationFacade
import com.example.familyconnectv2.models.CalendarActivity
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.calendar.CalendarViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class AgendaFragment : Fragment() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }

    private lateinit var addEventButton: FloatingActionButton
    private lateinit var addActivityButton: FloatingActionButton
    private lateinit var addGroupActivityButton: FloatingActionButton
    private var allActivities: List<CalendarActivity> = emptyList()


    private lateinit var floatingActionButtonManager: AnimationFacade

    companion object {
        fun newInstance() = AgendaFragment()
    }

    private lateinit var viewModel: AgendaViewModel
    private lateinit var viewModelcalendar: CalendarViewModel
    private lateinit var viewModelcreateGroup: CreateGroupViewModel
    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var activitiesAdapter: ActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agenda, container, false)

        addEventButton = view.findViewById(R.id.addbtn)
        addActivityButton = view.findViewById(R.id.addactivity)
        addGroupActivityButton = view.findViewById(R.id.addgroup)

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

        val recyclerView: RecyclerView = view.findViewById(R.id.agendarecycle)

        activitiesAdapter = ActivityAdapter(emptyList()) // Prvo inicijalno prazna lista
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = activitiesAdapter
        }

        viewModelcalendar = ViewModelProvider(this).get(CalendarViewModel::class.java)
        viewModelcalendar.initTokenManager(requireContext())

        viewModelcreateGroup = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        viewModelcreateGroup.initTokenManager(requireContext())

        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            viewModelcreateGroup.getUserIdByEmail(email)

        })

        viewModelcreateGroup.userId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != null) {
                viewModelcalendar.getActivitiesForDate(userId)
            }
        })

        viewModelcalendar.activities.observe(viewLifecycleOwner, Observer { activities ->
            activities?.let {
                // Ažuriranje podataka u adapteru
                allActivities = it.activities
                activitiesAdapter.setData(it.activities)
            }
        })

        val calendarView = view.findViewById<CalendarView>(R.id.calendar)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            updateAdapterForSelectedDate(selectedDate)
        }
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

    private fun convertDateToLocalDate(date: Date): LocalDate {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AgendaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}