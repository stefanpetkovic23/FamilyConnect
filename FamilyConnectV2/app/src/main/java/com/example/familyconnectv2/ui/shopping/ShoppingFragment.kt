package com.example.familyconnectv2.ui.shopping

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.ShoppingListAdapter
import com.example.familyconnectv2.adapters.ToDoGroupAdapter
import com.example.familyconnectv2.databinding.FragmentGroupToDoBinding
import com.example.familyconnectv2.databinding.FragmentShoppingBinding
import com.example.familyconnectv2.models.ToDoGroupRequest
import com.example.familyconnectv2.models.ToDoRequest
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel
import com.example.familyconnectv2.ui.groups.GalleryViewModel
import java.text.SimpleDateFormat
import java.util.*

class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding?=null
    private val binding get() = _binding!!

    private lateinit var spinner: Spinner

    companion object {
        fun newInstance() = ShoppingFragment()
    }
    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var viewModel: ShoppingViewModel

    private lateinit var tasksAdapter: ShoppingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(ShoppingViewModel::class.java)
        spinner = binding.spinnershop

        viewModel.initTokenManager(requireContext())

        val CreateGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)

        // Inicijalizujte TokenManager
        CreateGroupViewModel.initTokenManager(requireContext())

        tasksAdapter = ShoppingListAdapter(emptyList(),viewModel)
        binding.shoppingListView.adapter = tasksAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.shoppingListView.layoutManager = layoutManager

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        //val email = arguments?.getString("email")
        //Log.d("YourFragment", "Received email: $email")

        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout

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

                viewModel.getShoppingItemsForUser(userId)
                viewModel.shoppingItems.observe(viewLifecycleOwner, Observer { shoppingItems ->
                    shoppingItems?.let {
                        Log.d("ShoppingFragment", "Received shopping items: $shoppingItems")
                        if (shoppingItems.isNotEmpty()) {
                            val allItems = shoppingItems.flatMap { shoppingGroup ->
                                shoppingGroup.shoppingItems.map { shoppingItem ->
                                    Pair(shoppingGroup.groupName, shoppingItem)
                                }
                            }
                            Log.d("ShoppingFragment", "Received shopping items: $allItems")
                            tasksAdapter.updateData(allItems)
                        } else {
                            // Ako je lista prazna, obavestite korisnika ili preduzmite odgovarajuće radnje
                            // Na primer, prikažite prazan pogled ili poruku o praznoj listi
                        }
                    }
                })

                 fun loadShoppingItems() {

                     viewModel.getShoppingItemsForUser(userId)
                    viewModel.shoppingItems.observe(viewLifecycleOwner, Observer { shoppingItems ->
                        shoppingItems?.let {
                            Log.d("ShoppingFragment", "Received shopping items: $shoppingItems")
                            if (shoppingItems.isNotEmpty()) {
                                val allItems = shoppingItems.flatMap { shoppingGroup ->
                                    shoppingGroup.shoppingItems.map { shoppingItem ->
                                        Pair(shoppingGroup.groupName, shoppingItem)
                                    }
                                }
                                tasksAdapter.updateData(allItems)
                                tasksAdapter.notifyDataSetChanged()
                            } else {
                                // Ako je lista prazna, obavestite korisnika ili preduzmite odgovarajuće radnje
                                // Na primer, prikažite prazan pogled ili poruku o praznoj listi
                            }
                        }
                        // Nakon što završite sa osvežavanjem, obavestite SwipeRefreshLayout da se završilo
                        swipeRefreshLayout.isRefreshing = false

                    })
                }

                // Postavite boje osveživača
                swipeRefreshLayout.setColorSchemeResources(R.color.grey)

                // Postavite listener za osveživač
                swipeRefreshLayout.setOnRefreshListener {
                    loadShoppingItems()
                }

            }
        })



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


                        binding.addItemButton.setOnClickListener {

                            val itemName = binding.itemname.text.toString()
                            val quantity = binding.itemquantity.text.toString()
                            val isCompleted = false

                            if (groupId.isNotEmpty() && itemName.isNotEmpty() && quantity.isNotEmpty()) {
                                viewModel.addItemToShoppingList(groupId, itemName, quantity.toInt(), isCompleted)

                                binding.itemname.setText("")
                                binding.itemquantity.setText("")
                            } else {

                            }

                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Ova metoda se poziva kada nema izabranog elementa
                    }
                })

            }
        })






    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShoppingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}