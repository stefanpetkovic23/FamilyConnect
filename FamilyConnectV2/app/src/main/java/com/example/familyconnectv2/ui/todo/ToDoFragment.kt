package com.example.familyconnectv2.ui.todo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.familyconnectv2.R
import com.example.familyconnectv2.databinding.FragmentToDoBinding

class ToDoFragment : Fragment() {

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.privatetodocontainer.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_toDoFragment_to_privateToDoFragment)
        }

        binding.publictodocontainer.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_toDoFragment_to_groupToDoFragment)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}