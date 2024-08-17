package com.example.familyconnectv2.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.familyconnectv2.R
import com.example.familyconnectv2.databinding.FragmentLoginBinding
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.sp.TokenManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.initSharedPreferences(requireContext())

      /* viewModel.startDestination.observe(viewLifecycleOwner) { destination ->
           destination?.let {
               findNavController().navigate(it)
           }
       }*/

        binding.buttonLogin.setOnClickListener {
            val email= binding.tieEmail.text.toString().trim()
            val password=binding.tiePassword.text.toString().trim()
            val isChecked = binding.checkBox.isChecked

            viewModel.saveLoginState(isChecked)
            viewModel.loginUser(email, password, findNavController(),isChecked)

            sharedViewModel.setUserEmail(email)



        }


        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}