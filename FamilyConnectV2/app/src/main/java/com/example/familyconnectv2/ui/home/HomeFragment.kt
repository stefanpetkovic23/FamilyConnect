package com.example.familyconnectv2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.familyconnectv2.R
import com.example.familyconnectv2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val email = arguments?.getString("email")


        binding.calendar.setOnClickListener {  Navigation.findNavController(root).navigate(R.id.action_nav_home_to_calendarFragment)}
        binding.chat.setOnClickListener { Navigation.findNavController(root).navigate(R.id.action_nav_home_to_chatFragment) }
        binding.todo.setOnClickListener { Navigation.findNavController(root).navigate(R.id.action_nav_home_to_toDoFragment) }
        binding.shoppinglist.setOnClickListener { Navigation.findNavController(root).navigate(R.id.action_nav_home_to_shoppingFragment) }
        binding.budget.setOnClickListener { Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_slideshow)  }
        binding.group.setOnClickListener {
            val bundle = Bundle().apply {
            putString("email", email)
        }
            Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_gallery) }
        binding.profile.setOnClickListener { Navigation.findNavController(root).navigate(R.id.action_nav_home_to_profileFragment)  }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}