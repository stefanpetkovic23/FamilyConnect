package com.example.familyconnectv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.models.Group
import com.example.familyconnectv2.models.groupResponse
import java.text.SimpleDateFormat
import java.util.*

class GroupAdapter(private val groups: List<Group>) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textGroupName: TextView = itemView.findViewById(R.id.textGroupName)
        val textGroupStatus: TextView = itemView.findViewById(R.id.textGroupStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grouprecycleitem, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]

        // Postavljanje podataka
        holder.textGroupName.text = group.name

        // Postavite status grupe na osnovu potrebnih informacija
        if (group.createdAt!=null) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = inputFormat.parse(group.createdAt)
            val formattedDate = outputFormat.format(parsedDate)
            holder.textGroupStatus.text = formattedDate
        } else {
            "Neki drugi status"
        }

    }

    override fun getItemCount(): Int {
        return groups.size
    }
}