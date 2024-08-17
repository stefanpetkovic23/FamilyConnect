package com.example.familyconnectv2.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.decorators.OnTaskItemClickListener
import com.example.familyconnectv2.models.ToDoTasksItem
import com.example.familyconnectv2.ui.GroupToDoViewModel
import java.text.SimpleDateFormat

class ToDoAdapter(private var taskList: List<ToDoTasksItem>,private val viewModel : GroupToDoViewModel,private val listener: OnTaskItemClickListener) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd")
    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        val taskNameTextView: TextView = itemView.findViewById(R.id.taskNameTextView)
        val taskDescriptionTextView: TextView = itemView.findViewById(R.id.taskDescriptionTextView)
        val dueDateTextView: TextView = itemView.findViewById(R.id.dueDateTextView)


    }

    fun getTaskAtPosition(position: Int): ToDoTasksItem {
        return taskList[position]
    }

    fun updateTasks(newTasks: List<ToDoTasksItem>) {
            taskList = newTasks
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoAdapter.ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todorecycleitem, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoAdapter.ToDoViewHolder, position: Int) {
        val task = taskList[position]

        holder.taskCheckBox.isChecked = task.completed
        holder.taskNameTextView.text = task.title
        holder.taskDescriptionTextView.text = task.description
       // holder.dueDateTextView.text = task.dueDate
        val inputDate = inputDateFormat.parse(task.dueDate)
        if (inputDate != null) {
            val formattedDate = outputDateFormat.format(inputDate)
            val firstTenCharacters = formattedDate.take(10)
            holder.dueDateTextView.text = firstTenCharacters
        }
      /*  holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
               val clickedTaskId =task._id // Postavi trenutno kliknuti ID kada se CheckBox označi
                listener.onItemClick(clickedTaskId)
        holder.itemView.setBackgroundColor(Color.GREEN)
                Log.d("GroupToDoFragment", "Received userId: $clickedTaskId")
            } else {
                Log.d("GroupToDoFragment", "greska: ")
            }
        }*/
        holder.taskCheckBox.setOnClickListener {
            // Ako je čekiran, pozovi onItemClick
            if (holder.taskCheckBox.isChecked) {
                val clickedTaskId = task._id
                listener.onItemClick(clickedTaskId)
                holder.itemView.setBackgroundColor(Color.GREEN)
                Log.d("GroupToDoFragment", "Received userId: $clickedTaskId")
            }
        }
        holder.itemView.setBackgroundColor(if (task.completed) Color.GREEN else Color.RED)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}