package com.example.familyconnectv2.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.ToDoAdapter
import com.example.familyconnectv2.adapters.ToDoGroupAdapter
import com.example.familyconnectv2.databinding.FragmentGroupToDoBinding
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel
import com.example.familyconnectv2.ui.privateToDo.PrivateToDoViewModel
import com.example.familyconnectv2.decorators.OnTaskItemClickListener

class GroupToDoFragment : Fragment(), OnTaskItemClickListener {

    private var isFabVisible = true
    private var lastScrollY = 0

    private var _binding: FragmentGroupToDoBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: GroupToDoViewModel
    private lateinit var tasksAdapter: ToDoGroupAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupToDoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(GroupToDoViewModel::class.java)
        viewModel.initTokenManager(requireContext())

        binding.newTaskButton.setOnClickListener { Navigation.findNavController(root).navigate(R.id.action_groupToDoFragment_to_addToDoToGroupFragment) }

        tasksAdapter = ToDoGroupAdapter(emptyList(),viewModel,this@GroupToDoFragment)




        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createGroupViewModel = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        createGroupViewModel.initTokenManager(requireContext())

        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GroupToDoFragment", "Received email: $email")
            createGroupViewModel.getUserIdByEmail(email)

        })

         var fab = binding.newTaskButton
        var recycle  = binding.grouptaskRecyclerView

        recycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Ako se skroluje nagore i FAB je vidljiv, sakrijemo ga
                if (dy > 0 && isFabVisible) {
                    isFabVisible = false
                    fab.hide()
                }
                // Ako se skroluje nadole i FAB je sakriven, prikažemo ga
                else if (dy < 0 && !isFabVisible) {
                    isFabVisible = true
                    fab.show()
                }
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.grouptaskRecyclerView.layoutManager = layoutManager

        swipeRefreshLayout = binding.swipeRefreshLayoutgrouptodo

        // Postavljanje boje osveživača
        swipeRefreshLayout.setColorSchemeResources(R.color.grey)


        createGroupViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != null) {

                Log.d("GroupToDoFragment", "Received userId: $userId")

                viewModel.fetchTasks(userId)
                viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
                    if (tasks != null) {
                        //Log.d("GroupToDoFragment", "Received tasks: $tasks")
                        val todoTasksList = tasks.tasks
                        tasksAdapter = ToDoGroupAdapter(todoTasksList,viewModel,this@GroupToDoFragment)
                        binding.grouptaskRecyclerView.adapter = tasksAdapter
                        tasksAdapter.notifyDataSetChanged()
                        
                    } else {
                        Log.d("GroupToDoFragment", "Received null tasks")
                        tasksAdapter.notifyDataSetChanged()
                    }
                }

                 fun refreshRecyclerView() {
                    // Ovde pozovite odgovarajuću metodu za dobijanje podataka iz ViewModel-a
                    viewModel.fetchTasks(userId) // Prilagodite ovaj poziv vašim potrebama
                    viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
                        if (tasks != null) {
                            if (binding.grouptaskRecyclerView.adapter == null) {
                                val tasks1 = tasks.tasks
                                tasksAdapter = ToDoGroupAdapter(tasks1, viewModel, this@GroupToDoFragment)
                                binding.grouptaskRecyclerView.adapter = tasksAdapter
                            } else {
                                tasksAdapter.notifyDataSetChanged()
                            }
                        }
                        // Isključite osveživač nakon što je RecyclerView ažuriran
                        swipeRefreshLayout.isRefreshing = false
                    }
                }

                swipeRefreshLayout.setOnRefreshListener {
                    refreshRecyclerView()
                }

            }
        })


    }

    override fun onItemClick(taskId: String) {
        Log.d("GroupToDoFragment", "Clicked task ID: $taskId")
        viewModel.updateTask(taskId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}