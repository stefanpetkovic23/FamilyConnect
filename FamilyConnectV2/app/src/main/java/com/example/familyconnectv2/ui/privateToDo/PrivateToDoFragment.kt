package com.example.familyconnectv2.ui.privateToDo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.ToDoAdapter
import com.example.familyconnectv2.databinding.FragmentPrivateToDoBinding
import com.example.familyconnectv2.decorators.OnTaskItemClickListener
import com.example.familyconnectv2.models.ToDoTasksItem
import com.example.familyconnectv2.ui.GroupToDoViewModel
import com.example.familyconnectv2.ui.addtask.NewTaskSheet
import com.example.familyconnectv2.ui.addtask.TaskViewModel
import java.util.*

class PrivateToDoFragment : Fragment(), OnTaskItemClickListener {

    private var _binding: FragmentPrivateToDoBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var privatetaskViewModel: PrivateToDoViewModel
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var viewModel: GroupToDoViewModel

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivateToDoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.newTaskButton.setOnClickListener {
            val newTaskSheet = NewTaskSheet()

            newTaskSheet.setOnTaskAddedListener {
                refreshRecyclerView()
            }

            NewTaskSheet().show(childFragmentManager, "newTaskTag")
        }

        viewModel = ViewModelProvider(this).get(GroupToDoViewModel::class.java)
        viewModel.initTokenManager(requireContext())

        toDoAdapter = ToDoAdapter(emptyList(),viewModel,this@PrivateToDoFragment)

        return root
    }

    private fun refreshRecyclerView() {
        privatetaskViewModel.getAllTasks()
        privatetaskViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            if (binding.taskRecyclerView.adapter == null) {
                toDoAdapter = ToDoAdapter(tasks,viewModel,this@PrivateToDoFragment)
                binding.taskRecyclerView.adapter = toDoAdapter
            } else {
                toDoAdapter.updateTasks(tasks)
            }
        }

        swipeRefreshLayout.isRefreshing=false

    }

    fun addTask(title: String, description: String, dueDate: Date) {
        // Ovde implementirajte logiku za dodavanje zadatka
        taskViewModel.initTokenManager(requireContext())
        taskViewModel.addTask(title, description, dueDate)
        refreshRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        privatetaskViewModel =ViewModelProvider(this).get(PrivateToDoViewModel::class.java)
        privatetaskViewModel.initTokenManager(requireContext())
        privatetaskViewModel.getAllTasks()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.taskRecyclerView.layoutManager = layoutManager

        privatetaskViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            if (binding.taskRecyclerView.adapter == null) {
            toDoAdapter = ToDoAdapter(tasks,viewModel,this@PrivateToDoFragment)
            binding.taskRecyclerView.adapter = toDoAdapter
        } else {
            toDoAdapter.updateTasks(tasks)

        }
        }

         swipeRefreshLayout = binding.swipeRefreshLayoutprivatetodo

        // Postavljanje boje osveživača
        swipeRefreshLayout.setColorSchemeResources(R.color.grey)

        // Postavljanje listenera za osveživač
        swipeRefreshLayout.setOnRefreshListener {
            refreshRecyclerView()
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = toDoAdapter.getTaskAtPosition(position)
                privatetaskViewModel.deleteTask(task._id)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.taskRecyclerView)

    }


    override fun onItemClick(taskId: String) {
        Log.d("GroupToDoFragment", "Clicked task ID: $taskId")
        viewModel.updatePrivateTodo(taskId)
    }



}