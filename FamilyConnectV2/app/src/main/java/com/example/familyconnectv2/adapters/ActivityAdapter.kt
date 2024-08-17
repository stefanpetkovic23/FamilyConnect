package com.example.familyconnectv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.models.CalendarActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ActivityAdapter (private var activities: List<CalendarActivity>) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activityitem, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.bind(activity)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    fun setData(newData: List<CalendarActivity>) {
        activities = newData
        notifyDataSetChanged()
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.activitytitle)
        private val locationTextView: TextView = itemView.findViewById(R.id.activitylocation)
        private val noteTextView: TextView = itemView.findViewById(R.id.activitynote)
        private val dateTextView: TextView = itemView.findViewById(R.id.activitydate)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.delete)



        fun bind(calendarActivity: CalendarActivity) {
            val activity = calendarActivity.activity
            titleTextView.text = activity.title
            locationTextView.text = activity.location
            noteTextView.text = activity.note
            if (!activity.date.isNullOrEmpty()) {
                // Formatiranje datuma
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                try {
                    val inputDate = inputDateFormat.parse(activity.date)
                    if (inputDate != null) {
                        val formattedDate = outputDateFormat.format(inputDate)
                        dateTextView.text = formattedDate
                    } else {
                        dateTextView.text = ""
                    }
                } catch (e: ParseException) {
                    // Handle exception if parsing fails
                    e.printStackTrace()
                    dateTextView.text = ""
                }
            } else {
                dateTextView.text = ""
            }

            deleteImageView.isVisible = false
        }
    }
}