package com.example.familyconnectv2.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.familyconnectv2.R
import com.example.familyconnectv2.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel.initTokenManager(requireContext())

        binding.buttonRegister.setOnClickListener {
            val name = binding.tieUsername.text.toString().trim()
            val email = binding.tieEmail.text.toString().trim()
            val password = binding.tiePassword.text.toString().trim()
            val confirmpassword = binding.tieRepassword.text.toString().trim()

            if (password.isEmpty() || confirmpassword.isEmpty()) {

                Toast.makeText(requireContext(), "Unesite šifre.", Toast.LENGTH_SHORT).show()
            }else if( password!=confirmpassword){
                Toast.makeText(requireContext(), "Sifre se ne poklapaju!", Toast.LENGTH_SHORT).show()
            }
            else {
                // Ako su šifre unete, prosledite ih ViewModel-u za registraciju
                viewModel.setPasswords(password, confirmpassword)
                viewModel.registerUser(name, email, password, confirmpassword,findNavController())
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}