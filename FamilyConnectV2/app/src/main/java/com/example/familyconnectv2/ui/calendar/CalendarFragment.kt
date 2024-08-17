package com.example.familyconnectv2.ui.calendar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.VPAdapter
import com.example.familyconnectv2.databinding.FragmentCalendarBinding
import com.example.familyconnectv2.decorators.AnimationFacade
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel
import com.example.familyconnectv2.ui.groups.GalleryViewModel
import com.example.familyconnectv2.ui.monthcalendar.MonthCalendarViewModel
import com.example.familyconnectv2.ui.todo.AddToDoToGroupViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }

    private lateinit var addEventButton: FloatingActionButton
    private lateinit var addActivityButton: FloatingActionButton
    private lateinit var addGroupActivityButton: FloatingActionButton

    private lateinit var floatingActionButtonManager: AnimationFacade

    private val viewModelspinner: MonthCalendarViewModel by viewModels({ requireActivity() })
    val sharedViewModel: SharedViewModel by activityViewModels()

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicijalizacija ViewModel-a
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        viewModel.initTokenManager(requireContext())

        val CreateGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)

        CreateGroupViewModel.initTokenManager(requireContext())

        viewPager = root.findViewById(R.id.viewpager)
        tabLayout = root.findViewById(R.id.tablayout)
        val navHostFragment = childFragmentManager.findFragmentById(viewPager.id) as? NavHostFragment
        val navController = navHostFragment?.navController
        val adapter = VPAdapter(childFragmentManager, navController)
        viewPager.adapter = adapter

        // Povezivanje ViewPager-a s TabLayout-om
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.text = "Agenda"
        tabLayout.getTabAt(1)?.text = "Week"
        tabLayout.getTabAt(2)?.text = "Month"

        addEventButton = binding.addcbtn
        addActivityButton = binding.addactivitycbtn
        addGroupActivityButton = binding.addgroupcbtn

        floatingActionButtonManager = AnimationFacade(
            addEventButton,
            addActivityButton,
            addGroupActivityButton,
            rotateOpen,
            rotateClose,
            fromBottom,
            toBottom
        )



        addEventButton.setOnClickListener {
            floatingActionButtonManager.onAddButtonClicked()
        }



        addActivityButton.setOnClickListener {
            viewModelspinner.setShowSpinner(false)
            Navigation.findNavController(root).navigate(R.id.action_calendarFragment_to_addActivityFragment)
        }

        addGroupActivityButton.setOnClickListener {
            viewModelspinner.setShowSpinner(true)
            Navigation.findNavController(root).navigate(R.id.action_calendarFragment_to_addActivityFragment)
        }



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)



        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            createGroupViewModel.getUserIdByEmail(email)

        })

        createGroupViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != null) {
                viewModel.getActivitiesForDate(userId)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}