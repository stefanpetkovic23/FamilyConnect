package com.example.familyconnectv2.ui.todo

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.GroupAdapter
import com.example.familyconnectv2.databinding.FragmentAddToDoToGroupBinding
import com.example.familyconnectv2.models.ToDoGroupRequest
import com.example.familyconnectv2.models.ToDoRequest
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel
import com.example.familyconnectv2.ui.groups.GalleryViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddToDoToGroupFragment : Fragment() {

    private var _binding: FragmentAddToDoToGroupBinding? = null
    private val binding get() = _binding!!

    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var galleryViewModel: GalleryViewModel



    companion object {
        fun newInstance() = AddToDoToGroupFragment()
    }

    private lateinit var viewModel: AddToDoToGroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddToDoToGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Inicijalizacija ViewModel-a
        viewModel = ViewModelProvider(this).get(AddToDoToGroupViewModel::class.java)
        viewModel.initTokenManager(requireContext())

        viewModel.taskAdded.observe(viewLifecycleOwner) { taskadded ->
            if (taskadded) {
                findNavController().popBackStack()
            }
        }

        val CreateGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)


        CreateGroupViewModel.initTokenManager(requireContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        //val email = arguments?.getString("email")
        //Log.d("YourFragment", "Received email: $email")


        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)


        galleryViewModel.initTokenManager(requireContext())

        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            createGroupViewModel.getUserIdByEmail(email)

        })

        createGroupViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != null) {
                galleryViewModel.getGroupsByUserId(userId)
            }
        })


        galleryViewModel.groups.observe(viewLifecycleOwner, Observer { groups ->
            groups?.let {
                val groupNames = groups.map { it.name }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,groupNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerid.adapter = adapter

                binding.spinnerid.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // Dobijanje ID-a izabranog elementa
                        val groupId = groups[position]._id
                        Log.d("GroupId", "GroupId: $groupId")


                        binding.savebutton.setOnClickListener {
                            val taskName = binding.taskname.text.toString()
                            val taskDescription = binding.taskdescription.text.toString()
                            val taskDueDate = binding.taskduedate.text.toString()
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val taskDueDate1: Date = dateFormat.parse(taskDueDate)
                            val todoRequest = ToDoRequest(taskDescription, taskDueDate1, taskName)
                            val toDoGroupRequest = ToDoGroupRequest(groupId, todoRequest)

                            viewModel.addTaskToGroup(toDoGroupRequest)
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Ova metoda se poziva kada nema izabranog elementa
                    }
                })

            }
        })

        binding.taskduedate.setOnClickListener { showDatePickerDialog(view, binding.taskduedate) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showDatePickerDialog(view: View, editTextDate: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)


                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)


                editTextDate.setText(formattedDate)
            },
            year, month, dayOfMonth
        )


        datePickerDialog.show()
    }

}