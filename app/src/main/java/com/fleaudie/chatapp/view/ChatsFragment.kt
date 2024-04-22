package com.fleaudie.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.FragmentChatsBinding
import com.fleaudie.chatapp.viewmodel.ChatViewModel

class ChatsFragment : Fragment() {
    private lateinit var viewModel: ChatViewModel
    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}