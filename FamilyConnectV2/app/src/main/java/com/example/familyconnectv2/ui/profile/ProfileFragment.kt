package com.example.familyconnectv2.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.familyconnectv2.R
import com.example.familyconnectv2.databinding.FragmentProfileBinding
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.login.LoginViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        FirebaseApp.initializeApp(requireContext())
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initTokenManager(requireContext())
        viewModel.doSomethingWithToken()

        FirebaseApp.initializeApp(requireContext())
        if (FirebaseApp.getApps(requireContext()).isEmpty()) {
            FirebaseApp.initializeApp(requireContext())
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val deviceToken = task.result
                Log.d("DeviceToken", "Uređajski token: $deviceToken")

                // Ovde možete izvršiti dodatne radnje s uređajskim tokenom ako je potrebno
            } else {
                Log.e("DeviceToken", "Nije moguće dobiti uređajski token.")
            }
        }

        // Postavljanje OnClickListener za TextView za odjavu
        binding.odjava.setOnClickListener {
            // Pozivanje funkcije za odjavu iz ViewModel-a
            viewModel.logoutUser(findNavController())
        }

        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            viewModel.getUserData(email)

        })
        viewModel.userData.observe(viewLifecycleOwner, Observer { userInfo ->
            userInfo?.let {
                binding.emailEditText.text = it.name
                binding.nameEditText.text = it.email
            }
        })

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}