package com.example.familyconnectv2.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.familyconnectv2.R
import com.example.familyconnectv2.models.ActivityFull
import java.time.LocalDate

class CalendarAdapter(context: Context, private val dates: List<String>, private var activities: List<ActivityFull>) :
    ArrayAdapter<String>(context, 0, dates) {





    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_calendar, parent, false)
            holder = ViewHolder()
            holder.dateTextView = view.findViewById(R.id.dayTextView)
            holder.layout = view.findViewById(R.id.gridcontainer)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val date = getItem(position)
        holder.dateTextView.text = date


        if (date != null) {
            val isCurrentDay = checkIfCurrentDay(date)
            if (isCurrentDay) {
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_light))
            } else {

            }
        } else {

        }


        return view!!
    }
    private fun isDateInCurrentMonth(date: String): Boolean {
        val currentDate = LocalDate.now()
        val monthOfYear = currentDate.monthValue
        val year = currentDate.year
        val selectedDate = LocalDate.of(year, monthOfYear, date.toInt())
        return selectedDate.monthValue == monthOfYear
    }

    private class ViewHolder {
        lateinit var dateTextView: TextView
        lateinit var layout: ConstraintLayout

    }

    private fun checkIfCurrentDay(date: String): Boolean {
        val currentDate = LocalDate.now()
        val formattedDate = date.toIntOrNull()
        return currentDate.dayOfMonth == formattedDate
    }
}