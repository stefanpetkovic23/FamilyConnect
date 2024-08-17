package com.example.familyconnectv2.ui.addtask

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import com.example.familyconnectv2.R
import com.example.familyconnectv2.databinding.FragmentNewTaskSheetBinding
import com.example.familyconnectv2.ui.privateToDo.PrivateToDoFragment
import com.example.familyconnectv2.ui.privateToDo.PrivateToDoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class NewTaskSheet : BottomSheetDialogFragment() {

    private var listener: NewTaskListener? = null
    private lateinit var _binding: FragmentNewTaskSheetBinding
    private val binding get() = _binding!!

    //val privatetaskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

    fun setListener(listener: NewTaskListener) {
        this.listener = listener
    }

    private var onTaskAddedListener: (() -> Unit)? = null

    fun setOnTaskAddedListener(listener: () -> Unit) {
        onTaskAddedListener = listener
    }

    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    interface NewTaskListener {
        fun onTaskAdded(title: String, description: String, dueDate: Date)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.savebutton.setOnClickListener {
            val title = binding.taskname.text.toString()
            val description = binding.taskdescription.text.toString()
            val dueDateString = binding.taskduedate.text.toString()

            // Parsiranje teksta u Date objekat
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dueDate: Date = dateFormat.parse(dueDateString)

            // Pozovite metod interfejsa za slanje podataka natrag u fragment
            (parentFragment as? PrivateToDoFragment)?.addTask(title, description, dueDate)
            onTaskAddedListener?.invoke()

            // Zatvorite dijalog
            dismiss()
        }

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dodajte svoju logiku ovde

        // Postavljanje klika na polje datuma kako biste pozvali dijalog kalendara
        binding.taskduedate.setOnClickListener {
            showDatePickerDialog()
        }

        // Dodajte logiku za čuvanje zadatka itd.
    }


    // Metoda za prikaz dijaloga kalendara
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                // Postavljanje odabranog datuma u polje
                calendar.set(year, month, dayOfMonth)
                updateDueDate()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    // Metoda za ažuriranje prikaza odabranog datuma
    private fun updateDueDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Formatiranje datuma
        val formattedDate = dateFormat.format(calendar.time)
        (binding.taskduedate as? TextInputEditText)?.setText(formattedDate)
    }

}