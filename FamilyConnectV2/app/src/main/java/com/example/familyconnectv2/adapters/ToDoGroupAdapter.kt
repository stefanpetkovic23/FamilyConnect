package com.example.familyconnectv2.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.decorators.OnTaskItemClickListener
import com.example.familyconnectv2.models.ToDoTasks
import com.example.familyconnectv2.models.ToDoTasksItem
import com.example.familyconnectv2.ui.GroupToDoViewModel
import java.text.SimpleDateFormat

class ToDoGroupAdapter (private var tasks: List<ToDoTasks>,private val viewModel : GroupToDoViewModel,private val listener: OnTaskItemClickListener) :
    RecyclerView.Adapter<ToDoGroupAdapter.TaskViewHolder>() {



    private var clickedTaskId: String? = null
    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd")
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
        val taskNameTextView: TextView = itemView.findViewById(R.id.taskNameTextView)
        val taskDescriptionTextView: TextView = itemView.findViewById(R.id.taskDescriptionTextView)
        val dueDateTextView: TextView = itemView.findViewById(R.id.dueDateTextView)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.todogrouprecycle, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]


        val inputDate = inputDateFormat.parse(currentTask.task.dueDate)
        if (inputDate != null) {
            val formattedDate = outputDateFormat.format(inputDate)
            val firstTenCharacters = formattedDate.take(10)
            holder.dueDateTextView.text = firstTenCharacters
        }
        holder.groupNameTextView.text = currentTask.group?.name
        holder.taskNameTextView.text = currentTask.task.title
        holder.taskDescriptionTextView.text = currentTask.task.description
        holder.taskCheckBox.isChecked = currentTask.task.completed
       /* holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                clickedTaskId = currentTask.task._id // Postavi trenutno kliknuti ID kada se CheckBox označi
                listener.onItemClick(currentTask.task._id)
                holder.itemView.setBackgroundColor(Color.GREEN)
                Log.d("GroupToDoFragment", "Received userId: $clickedTaskId")
            } else {
                Log.d("GroupToDoFragment", "mucak: $clickedTaskId")
            }
        }
        holder.itemView.setBackgroundColor(if (currentTask.task.completed) Color.GREEN else Color.RED)*/
        holder.taskCheckBox.setOnClickListener {
            // Ako je čekiran, pozovi onItemClick
            if (holder.taskCheckBox.isChecked) {
                val clickedTaskId = currentTask.task._id
                listener.onItemClick(clickedTaskId)
                holder.itemView.setBackgroundColor(Color.GREEN)
                Log.d("GroupToDoFragment", "Received userId: $clickedTaskId")
            }
        }
        holder.itemView.setBackgroundColor(if (currentTask.task.completed) Color.GREEN else Color.RED)

    }

    fun getClickedTaskId(): String? {
        return clickedTaskId
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<ToDoTasks>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}