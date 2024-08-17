package com.example.familyconnectv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.decorators.OnChatClickListener
import com.example.familyconnectv2.models.CalendarActivity
import com.example.familyconnectv2.models.Chat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter (private var chatList: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var chatClickListener: OnChatClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chatitem, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.bind(chat)
        holder.itemView.setOnClickListener {
            chatClickListener?.onChatClick(chat._id) // Pozivamo metodu onChatClick kada je stavka kliknuta
        }
    }

    fun setOnChatClickListener(listener: OnChatClickListener) {
        this.chatClickListener = listener
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewGroupName: TextView = itemView.findViewById(R.id.textViewGroupName)
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewSender: TextView = itemView.findViewById(R.id.textViewSender)
        private val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    chatClickListener?.onChatClick(chatList[position]._id)
                }
            }
        }

        fun bind(chat: Chat) {
            textViewGroupName.text = chat.chatName
            textViewDate.text = chat.latestMessage?.createdAt
            if(chat.latestMessage?.sender!=null)
            {
                textViewSender.text = chat.latestMessage?.sender?.name + ": "
            }else
            {

            }


            textViewMessage.text = chat.latestMessage?.content
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


            try {

                if(chat.latestMessage!=null)
                { val inputDate = inputDateFormat.parse(chat.latestMessage?.createdAt)
                    if (inputDate != null && chat.latestMessage!=null) {
                        val today = Calendar.getInstance()
                        val messageDate = Calendar.getInstance().apply { time = inputDate }

                        val formattedDate = if (today.get(Calendar.YEAR) == messageDate.get(Calendar.YEAR) &&
                            today.get(Calendar.MONTH) == messageDate.get(Calendar.MONTH) &&
                            today.get(Calendar.DAY_OF_MONTH) == messageDate.get(Calendar.DAY_OF_MONTH)) {
                            // Poruka je poslata danas - prikaži samo sat i minut
                            outputTimeFormat.format(inputDate)
                        } else {
                            // Prikazuje se godina, mesec i dan
                            outputDateFormat.format(inputDate)
                        }

                        textViewDate.text = formattedDate
                    } else {
                        textViewDate.text = ""
                    }}


            } catch (e: ParseException) {
                // Handle exception if parsing fails
                e.printStackTrace()
                textViewDate.text = ""
            }
        }



    }

    fun setData(newData: List<Chat>) {
        chatList = newData
        notifyDataSetChanged()
    }
}