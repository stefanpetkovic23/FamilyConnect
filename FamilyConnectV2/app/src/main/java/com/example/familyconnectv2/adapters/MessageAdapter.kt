package com.example.familyconnectv2.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.models.CalendarActivity
import com.example.familyconnectv2.models.ChatMessageDetails

class MessageAdapter (private var messages: List<ChatMessageDetails>, private val currentUserId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 0
        private const val VIEW_TYPE_OTHER_MESSAGE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sendmessageitem, parent, false)
                MyMessageViewHolder(view)
            }
            VIEW_TYPE_OTHER_MESSAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.recievedmessageitem, parent, false)
                OtherMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun setData(newData: List<ChatMessageDetails>) {
        messages = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is MyMessageViewHolder -> {
                // Bind data for message sent by the current user
                holder.bind(message)
            }
            is OtherMessageViewHolder -> {
                // Bind data for message sent by other users
                holder.bind(message)
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.sender._id == currentUserId) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    inner class MyMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessageSent: TextView = itemView.findViewById(R.id.textMessageSent)


        fun bind(message: ChatMessageDetails) {
            // Bind data for message sent by the current user
            textMessageSent.text = message.content

        }
    }

    inner class OtherMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessageReceived: TextView = itemView.findViewById(R.id.textMessageReceived)
        private val textMessageSender: TextView = itemView.findViewById(R.id.textSenderName)

        fun bind(message: ChatMessageDetails) {
            // Bind data for message sent by other users
            textMessageReceived.text = message.content
            textMessageSender.text = message.sender.name

        }
    }
}