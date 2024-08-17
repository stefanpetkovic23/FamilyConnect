package com.example.familyconnectv2.ui.groups

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.UsersAdapter
import com.example.familyconnectv2.databinding.CreategroupdialogBinding
import com.example.familyconnectv2.models.ToDoTasksItem
import com.example.familyconnectv2.models.userResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGroupDialog: Fragment() {

    private val userService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager
    private lateinit var userAdapter: UsersAdapter
    private lateinit var back : ImageView

    private lateinit var viewModel: CreateGroupViewModel
    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var viewModelcreateGroup: CreateGroupViewModel

    private var currentUserEmail: String = ""

    private val selectedEmailsList = mutableListOf<String>()

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CreategroupdialogBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        viewModel.initTokenManager(requireContext())
        //viewModel.getUserIdByEmail("test1@gmail.com")

        back = binding.backbutton

        back.setOnClickListener {
            findNavController().popBackStack()
        }


        viewModelcreateGroup = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        viewModelcreateGroup.initTokenManager(requireContext())

        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            viewModelcreateGroup.getUserIdByEmail(email)
            userAdapter.setCurrentUserEmail(email)

        })

        Log.d("GalleryFragment", "CurrentEmail: $currentUserEmail")
        // Inicijalizujte adapter
        val usersAdapter = UsersAdapter { selectedUsersText ->
            // Postavite tekst u EditText na fragmentu ako se nešto promenilo
            if (binding.editTextSearch.text.toString() != selectedUsersText) {
                binding.editTextSearch.setText(selectedUsersText)

            }
        }
        userAdapter = usersAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUsers.layoutManager = layoutManager
        binding.recyclerViewUsers.adapter = userAdapter

        // Implementirajte logiku fragmenta ovde
        getAllUsers()

        binding.textViewCreateGroup.setOnClickListener {
            // Dobavite odabrane korisnike iz adaptera
            val selectedUserIds = userAdapter.getSelectedUserIdeas()

            // Dobavite naziv grupe iz EditText-a
            val groupName = binding.editTextGroupName.text.toString()
            if (groupName.isEmpty() || selectedUserIds.isEmpty()) {
                // Prikazivanje Toast poruke da obavesti korisnika
                Toast.makeText(requireContext(), "Unesite naziv grupe i izaberite članove", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pozovite funkciju za kreiranje grupe
            createGroup(groupName, selectedUserIds)
            createGroupChat(groupName,selectedUserIds)
            viewModel.groupCreated.observe(viewLifecycleOwner) { groupCreated ->
                if (groupCreated) {
                    findNavController().popBackStack()
                }
            }
        }

        return binding.root
    }

    private fun getAllUsers() {
        userService.getAllUsers().enqueue(object : Callback<List<userResponse>> {
            override fun onResponse(
                call: Call<List<userResponse>>,
                response: Response<List<userResponse>>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()

                    // Ažurirajte adapter sa novim podacima
                    users?.let {
                        userAdapter.updateUsers(it)
                        userAdapter.notifyDataSetChanged()
                    }

                    Log.d("UserViewModel", "Lista korisnika: $users")
                } else {
                    // Neuspesan odgovor
                    // Obradite grešku prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("UserViewModel", "Greška prilikom dohvatanja korisnika. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<List<userResponse>>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("UserViewModel", "Greška prilikom slanja zahteva za dohvatanje korisnika", t)
            }
        })
    }

    private fun createGroup(name: String, users: List<String>) {
        viewModel.createGroup(name, users)
    }

    private fun createGroupChat(name: String, users: List<String>) {
        viewModel.createGroupChat(name, users)
    }
}