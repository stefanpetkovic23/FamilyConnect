package com.example.familyconnectv2.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.GroupAdapter
import com.example.familyconnectv2.adapters.ToDoAdapter
import com.example.familyconnectv2.databinding.FragmentGalleryBinding
import com.example.familyconnectv2.sp.SharedViewModel

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var groupsAdapter: GroupAdapter

    val sharedViewModel: SharedViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        val CreateGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)

        // Inicijalizujte TokenManager
        CreateGroupViewModel.initTokenManager(requireContext())

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.newGroupButton.setOnClickListener {
             Navigation.findNavController(root).navigate(R.id.action_nav_gallery_to_createGroupDialog)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        //val email = arguments?.getString("email")
        //Log.d("YourFragment", "Received email: $email")

        // Inicijalizujte GalleryViewModel
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        // Inicijalizujte TokenManager
        galleryViewModel.initTokenManager(requireContext())

        // Inicijalizujte RecyclerView i povežite ga s adapterom
        groupsAdapter = GroupAdapter(emptyList()) // Prilagodite prema vašem adapteru
        binding.groupRecyclerView.adapter = groupsAdapter

        //binding.groupRecyclerView.adapter = groupsAdapter

        // Postavite LinearLayoutManager
        val layoutManager = LinearLayoutManager(requireContext())
        binding.groupRecyclerView.layoutManager = layoutManager


        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            createGroupViewModel.getUserIdByEmail(email)

        })

        createGroupViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != null) {
                // Sada možete koristiti userId kako vam je potrebno
                // Na primer, pozovite funkciju za dobijanje grupa sa userId
                galleryViewModel.getGroupsByUserId(userId)
            }
        })
        //galleryViewModel.getGroupsByUserId("656cd2bd8f6b661736810a5b" )



        galleryViewModel.groups.observe(viewLifecycleOwner, Observer { groups ->
            groups?.let {
                val adapter = GroupAdapter(groups)
                binding.groupRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
    }
}