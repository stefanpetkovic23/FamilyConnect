package com.example.familyconnectv2.ui.chat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.ChatAdapter
import com.example.familyconnectv2.databinding.FragmentChatBinding
import com.example.familyconnectv2.decorators.OnChatClickListener
import com.example.familyconnectv2.models.Chat
import com.example.familyconnectv2.ui.chatmassages.ChatMessagesFragment

class ChatFragment : Fragment(), OnChatClickListener {

    companion object {
        fun newInstance() = ChatFragment()
    }
    private lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        val root =  binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        viewModel.initTokenManager(requireContext()) // Inicijalizacija TokenManager-a


        binding.recyclerViewChat.layoutManager = LinearLayoutManager(requireContext())

        chatAdapter = ChatAdapter(emptyList())
        chatAdapter.setOnChatClickListener(this)
        binding.recyclerViewChat.adapter = chatAdapter

        viewModel.fetchChats()

        // Observer koji osluškuje promene u listi chatova
        viewModel.chats.observe(viewLifecycleOwner, Observer { chats ->
            // Ovde možete obraditi dohvaćene chatove
            // Na primer, možete ih prikazati na UI-u ili izvršiti druge operacije
            chats?.let {
                chatAdapter.setData(chats)

            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onChatClick(chatid: String) {
        val bundle = Bundle()
        bundle.putString("chatId", chatid)
        findNavController().navigate(R.id.action_chatFragment_to_chatMessagesFragment, bundle)
    }

}