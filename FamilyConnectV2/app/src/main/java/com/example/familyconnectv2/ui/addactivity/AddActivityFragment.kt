package com.example.familyconnectv2.ui.addactivity

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
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.familyconnectv2.R
import com.example.familyconnectv2.databinding.FragmentAddActivityBinding
import com.example.familyconnectv2.decorators.SpinnerVisibilityListener
import com.example.familyconnectv2.models.ActivityDetails
import com.example.familyconnectv2.models.ToDoGroupRequest
import com.example.familyconnectv2.models.ToDoRequest
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel
import com.example.familyconnectv2.ui.groups.GalleryViewModel
import com.example.familyconnectv2.ui.monthcalendar.MonthCalendarViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddActivityFragment : Fragment() {

    private lateinit var spinner: Spinner
    private lateinit var savebtn: Button
    private lateinit var binding: FragmentAddActivityBinding

    companion object {
        fun newInstance(showSpinner: Boolean): AddActivityFragment {
            val fragment = AddActivityFragment()
            val args = Bundle().apply {
                putBoolean("showSpinner", showSpinner)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private val monthCalendarViewModel: MonthCalendarViewModel by viewModels({ requireActivity() })


    val sharedViewModel: SharedViewModel by activityViewModels()
    //val viewModelmonth: MonthCalendarViewModel by activityViewModels()
    private lateinit var galleryViewModel: GalleryViewModel

    private lateinit var viewModel: AddActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val CreateGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)

        // Inicijalizujte TokenManager
        CreateGroupViewModel.initTokenManager(requireContext())
        viewModel = ViewModelProvider(this).get(AddActivityViewModel::class.java)
        viewModel.initTokenManager(requireContext())

        viewModel.activityAdded.observe(viewLifecycleOwner){activityadded ->
            if(activityadded)
                findNavController().popBackStack()
        }

        spinner = binding.spinneridactivity

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val createGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        //val email = arguments?.getString("email")
        //Log.d("YourFragment", "Received email: $email")


        binding.taskduedateactivity.setOnClickListener {
            // Pozivanje funkcije za prikaz DatePickerDialog-a
            showDatePickerDialog(view, binding.taskduedateactivity)
        }







        // Inicijalizujte GalleryViewModel
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        // Inicijalizujte TokenManager
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

        //monthCalendarViewModel.setShowSpinner(false)
        monthCalendarViewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            // Ažurirajte UI prema vrednosti showSpinnerc
            Log.d("GalleryFragment", "Received spinner boolean at addactivity: $showSpinner")

            if (showSpinner) {
                spinner.visibility = View.VISIBLE
                galleryViewModel.groups.observe(viewLifecycleOwner, Observer { groups ->
                    groups?.let {
                        val groupNames = groups.map { it.name }
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,groupNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter

                        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                // Dobijanje ID-a izabranog elementa
                                val groupId = groups[position]._id // Pretpostavljajući da je ID atribut u modelu grupe
                                Log.d("GroupId", "GroupId: $groupId")


                                binding.savebutton.setOnClickListener {
                                    val taskDueDate = binding.taskduedateactivity.text.toString()
                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    val taskDueDate1: Date = dateFormat.parse(taskDueDate)
                                    val location = binding.activitylocation.text.toString()
                                    val image = "image.jpg"
                                    val note = binding.activitynote.text.toString()
                                    val title = binding.activitytitle.text.toString()

                                    val activityDetails = ActivityDetails(title,image,location,note,taskDueDate1)
                                    viewModel.addActivityToGroup(groupId,activityDetails)

                                    findNavController().navigateUp()
                                }


                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Ova metoda se poziva kada nema izabranog elementa
                            }
                        })

                        //spinner.visibility = View.INVISIBLE

                    }
                })
            } else {
                spinner.visibility = View.GONE
                binding.savebutton.setOnClickListener {
                    val taskDueDate = binding.taskduedateactivity.text.toString()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val taskDueDate1: Date = dateFormat.parse(taskDueDate)
                    val location = binding.activitylocation.text.toString()
                    val image = "image.jpg"
                    val note = binding.activitynote.text.toString()
                    val title = binding.activitytitle.text.toString()

                    val activityDetails = ActivityDetails(title,image,location,note,taskDueDate1)
                    viewModel.addActivity(activityDetails)
                }
            }
        })



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddActivityViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun showDatePickerDialog(view: View, editTextDate: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Kreiranje DatePickerDialog-a
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Ovaj blok koda će biti pozvan kada korisnik izabere datum
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                // Konverzija izabranog datuma u format koji vam odgovara
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)

                // Postavljanje izabranog datuma u EditText
                editTextDate.setText(formattedDate)
            },
            year, month, dayOfMonth
        )

        // Prikaži DatePickerDialog
        datePickerDialog.show()
    }

}