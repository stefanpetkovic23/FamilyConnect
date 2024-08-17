package com.example.familyconnectv2.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar

class WeekAdapter(private val dates: List<LocalDate>, private val listener: OnItemListener) : RecyclerView.Adapter<WeekAdapter.CalendarViewHolder>() {

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate)
    }

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val dateTextView: TextView = itemView.findViewById(R.id.currentDateTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition, dates[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.weekcalendar, parent, false)
        return CalendarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = dates[position]
        val formatter = DateTimeFormatter.ofPattern("EEE dd", Locale.getDefault())
        val formattedDate = date.format(formatter)
        holder.dateTextView.text = formattedDate
    }

    override fun getItemCount(): Int {
        return dates.size
    }
}