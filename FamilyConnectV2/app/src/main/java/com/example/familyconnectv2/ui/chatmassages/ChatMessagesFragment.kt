package com.example.familyconnectv2.ui.chatmassages

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.MessageAdapter
import com.example.familyconnectv2.models.SendMessageRequest
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.ui.groups.CreateGroupViewModel

class ChatMessagesFragment : Fragment() {

    companion object {
        fun newInstance() = ChatMessagesFragment()
    }

    private lateinit var viewModel: ChatMessagesViewModel
    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var viewModelcreateGroup: CreateGroupViewModel
    private lateinit var adapter: MessageAdapter

    private lateinit var chatId: String

    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            // Pozivamo osvežavanje poruka svake sekunde
            viewModel.getAllMessages(chatId)
            handler.postDelayed(this, 1000) // Postavljanje novog osvežavanja nakon 1 sekunde
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatId = it.getString("chatId") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ChatMessagesFragment", "Received chat ID: $chatId")
        viewModel = ViewModelProvider(this).get(ChatMessagesViewModel::class.java)
        viewModel.initTokenManager(requireContext())

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMessages)
        val editTextMessage: EditText = view.findViewById(R.id.editTextMessage)
        val buttonSendMessage: Button = view.findViewById(R.id.buttonSendMessage)


        viewModel.getAllMessages(chatId)

        viewModelcreateGroup = ViewModelProvider(this).get(CreateGroupViewModel::class.java)
        viewModelcreateGroup.initTokenManager(requireContext())

        sharedViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            Log.d("GalleryFragment", "Received email: $email")
            viewModelcreateGroup.getUserIdByEmail(email)

        })

        viewModelcreateGroup.userId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != null) {
                adapter = MessageAdapter(emptyList(),userId)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
                handler.postDelayed(refreshRunnable, 1000)

                viewModel.chatMessages.observe(viewLifecycleOwner, Observer { chatMessages ->
                    // Ažuriranje adaptera sa novim podacima kada se promene
                    adapter.setData(chatMessages)
                })
            }
        })

        buttonSendMessage.setOnClickListener {
            val editTextMessage: EditText = view.findViewById(R.id.editTextMessage)
            val messageContent = editTextMessage.text.toString()
            if (messageContent.isNotBlank()) {
                val request = SendMessageRequest(content = messageContent, chatId = chatId)
                viewModel.sendMessage(request)
                editTextMessage?.text?.clear()
            } else {
                // Prikaži poruku da je unos prazan
                Toast.makeText(requireContext(), "Unesite poruku", Toast.LENGTH_SHORT).show()
            }
        }




    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Uklanjanje osvežavanja kada Fragment više nije vidljiv
        handler.removeCallbacks(refreshRunnable)
    }

    override fun onPause() {
        super.onPause()
        // Uklanjanje osvežavanja kada Fragment više nije aktivan
        handler.removeCallbacks(refreshRunnable)
    }



}