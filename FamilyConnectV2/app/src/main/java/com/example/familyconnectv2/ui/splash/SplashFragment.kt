package com.example.familyconnectv2.ui.splash

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.familyconnectv2.MainActivity
import com.example.familyconnectv2.R
import com.example.familyconnectv2.databinding.FragmentHomeBinding
import com.example.familyconnectv2.databinding.FragmentSplashBinding
import com.example.familyconnectv2.ui.home.HomeViewModel
import com.example.familyconnectv2.ui.login.LoginViewModel

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private lateinit var viewModel: LoginViewModel


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(SplashViewModel::class.java)

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        viewModel.initSharedPreferences(requireContext())

        // Proveri status logovanja
      /*  if (viewModel.isLoggedIn()) {
            findNavController().navigate(R.id.action_splashFragment_to_nav_home)
        }*/

        binding.LoginBTN.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_splashFragment_to_loginFragment)
        }

        binding.RegisterBTN.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_splashFragment_to_registerFragment)

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}