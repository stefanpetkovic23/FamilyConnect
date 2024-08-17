package com.example.familyconnectv2.ui.monthcalendar

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
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.Navigation
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.CalendarAdapter
import com.example.familyconnectv2.decorators.AnimationFacade
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.adapters.ActivityAdapter
import com.example.familyconnectv2.adapters.PrivateActivityAdapter
import com.example.familyconnectv2.databinding.FragmentHomeBinding
import com.example.familyconnectv2.databinding.FragmentMonthCalendarBinding
import com.example.familyconnectv2.decorators.SpinnerVisibilityFacade
import com.example.familyconnectv2.decorators.SpinnerVisibilityListener
import com.example.familyconnectv2.models.CalendarActivity
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.calendar.CalendarViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel
import androidx.lifecycle.Observer
import com.example.familyconnectv2.models.ActivityFull

class MonthCalendarFragment : Fragment(){

    private var _binding: FragmentMonthCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }

    private lateinit var addEventButton: FloatingActionButton
    private lateinit var addActivityButton: FloatingActionButton
    private lateinit var addGroupActivityButton: FloatingActionButton

    val spinnerVisibilityFacade = SpinnerVisibilityFacade()
    private lateinit var activityList: List<ActivityFull>

    private val viewModelmonth: MonthCalendarViewModel by activityViewModels()

    private lateinit var floatingActionButtonManager: AnimationFacade

    companion object {
        fun newInstance() = MonthCalendarFragment()
    }

    private lateinit var viewModel: MonthCalendarViewModel

    private lateinit var viewModelcalendarmonth: CalendarViewModel
    private lateinit var viewModelcreateGroupmonth: CreateGroupViewModel
    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var activitiesAdapter: PrivateActivityAdapter
    private var allActivities: List<CalendarActivity> = emptyList()

    private lateinit var monthYearTextView: TextView
    private lateinit var calendarGridView: GridView
    private lateinit var previousMonthButton: Button
    private lateinit var nextMonthButton: Button

    private var currentDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        monthYearTextView = binding.monthYearTextView
        calendarGridView = binding.calendarGridView
        previousMonthButton = binding.previousMonthButton
        nextMonthButton = binding.nextMonthButton

        activityList = emptyList()
        // Postavljanje početnog prikaza
        updateCalendar(activityList)

        // Dodavanje slušalaca na dugmad
        previousMonthButton.setOnClickListener {
            currentDate = currentDate.minusMonths(1)
            updateCalendar(activityList)
        }

        nextMonthButton.setOnClickListener {
            currentDate = currentDate.plusMonths(1)
            updateCalendar(activityList)
        }

        addEventButton = binding.addbtnmonth
        addActivityButton = binding.addactivitymonth
        addGroupActivityButton = binding.addgroupmonth

        floatingActionButtonManager = AnimationFacade(
            addEventButton,
            addActivityButton,
            addGroupActivityButton,
            rotateOpen,
            rotateClose,
            fromBottom,
            toBottom
        )



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MonthCalendarViewModel::class.java)
        viewModel.initTokenManager(requireContext())

        val recyclerView: RecyclerView = view.findViewById(R.id.recycleprivateactivity)

        activitiesAdapter = PrivateActivityAdapter(emptyList(),viewModel)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            adapter = activitiesAdapter
        }

        viewModelcalendarmonth = ViewModelProvider(this).get(CalendarViewModel::class.java)
        viewModelcalendarmonth.initTokenManager(requireContext())

        viewModelcreateGroupmonth = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        viewModelcreateGroupmonth.initTokenManager(requireContext())

        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            viewModelcreateGroupmonth.getUserIdByEmail(email)

        })

        viewModelcreateGroupmonth.userId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != null) {
                viewModel.fetchActivities(userId)
            }
        })

        viewModel.activities.observe(viewLifecycleOwner, Observer { activities ->
            activities?.let {
                // Ažuriranje podataka u adapteru
               // allActivities = it.activities
                activitiesAdapter.setData(activities)
                if (activities.isNotEmpty()) {
                    updateCalendar(activities)
                }

            }
        })



    }



    private fun updateCalendar(activities: List<ActivityFull>) {
        Log.d("MonthCalendarFragment", "Broj aktivnosti: ${activities.size}")
        val monthYearFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())
        monthYearTextView.text = currentDate.format(monthYearFormatter)

        // Pravljenje liste datuma za prikaz u gridu
        val dates = mutableListOf<String>()
        val daysInMonth = currentDate.lengthOfMonth()
        val firstDayOfMonth = LocalDate.of(currentDate.year, currentDate.month, 1)
        val startDayOfWeek = firstDayOfMonth.dayOfWeek.value

        // Dodavanje praznih mesta za dane pre prvog dana meseca
        for (i in 1 until startDayOfWeek) {
            dates.add("")
        }

        // Dodavanje dana u mesec
        for (dayOfMonth in 1..daysInMonth) {
            dates.add(dayOfMonth.toString())
        }

        // Postavljanje adaptera za grid
        val adapter = CalendarAdapter(requireContext(), dates,activities)
        calendarGridView.adapter = adapter
    }


}