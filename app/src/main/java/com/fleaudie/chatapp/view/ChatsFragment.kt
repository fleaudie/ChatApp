package com.fleaudie.chatapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.adapters.ChatAdapter
import com.fleaudie.chatapp.databinding.FragmentChatsBinding
import com.fleaudie.chatapp.helpers.ChatHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    private lateinit var binding: FragmentChatsBinding
    private lateinit var chatHelper: ChatHelper
    private lateinit var adapter: ChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatHelper = ChatHelper()

        adapter = ChatAdapter(emptyList(), requireContext())

        binding.rcyChatList.layoutManager = LinearLayoutManager(requireContext())
        binding.rcyChatList.adapter = adapter

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            chatHelper.listenForLastMessages(
                userId,
                onSuccess = { chatInfoList ->
                    if (chatInfoList.isNotEmpty()) {
                        adapter.updateData(chatInfoList)
                    } else {
                        Log.e("ChatFragment", "Received empty data")
                    }
                },
                onFailure = { exception ->
                    Log.e("ChatFragment", "Error fetching data: $exception")
                }
            )

        }
    }

}

