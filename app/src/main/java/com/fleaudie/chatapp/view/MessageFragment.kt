package com.fleaudie.chatapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.adapters.MessageAdapter
import com.fleaudie.chatapp.data.datasource.AuthDataSource
import com.fleaudie.chatapp.data.datasource.ChatDataSource
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.fleaudie.chatapp.data.repository.ChatRepository
import com.fleaudie.chatapp.databinding.FragmentMessageBinding
import com.fleaudie.chatapp.viewmodel.CodeViewModel
import com.fleaudie.chatapp.viewmodel.MessageViewModel
import com.google.firebase.auth.FirebaseAuth

class MessageFragment : Fragment() {
    private lateinit var binding : FragmentMessageBinding
    private lateinit var name: String
    private lateinit var adapter: MessageAdapter
    private lateinit var viewModel: MessageViewModel
    private lateinit var receiverId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)
        val args: MessageFragmentArgs by navArgs()
        name = args.name
        receiverId = args.uid
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtMsgContactName.text = name

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        adapter = MessageAdapter(emptyList(), requireContext())
        binding.rcyMessages.adapter = adapter

        binding.rcyMessages.layoutManager = LinearLayoutManager(requireContext())
        viewModel.messageList.observe(viewLifecycleOwner, Observer {
            adapter.updateMessages(it)
            binding.rcyMessages.scrollToPosition(it.size - 1)
        })

        binding.floatSendMessage.setOnClickListener {
            val text = binding.editTextMessage.text.toString()
            if (text.isNotEmpty()){
                if (currentUserId != null) {
                    viewModel.sendMessage(currentUserId, receiverId, text,
                        onSuccess = {
                            Toast.makeText(requireContext(), "message send", Toast.LENGTH_SHORT).show()
                        },  onFailure = {
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        })
                }
            }
        }



        if (currentUserId != null) {
            viewModel.fetchMessages(currentUserId, receiverId,
                onSuccess = { messages ->
                    val sortedMessages = messages.sortedBy { it.timestamp }
                    adapter.updateMessages(sortedMessages)
                    binding.rcyMessages.scrollToPosition(sortedMessages.size - 1)
                },
                onFailure = { error ->
                    Log.e("MessageFragment", "Error fetching messages: $error")
                    Toast.makeText(requireContext(), "Error fetching messages: $error", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = ChatRepository(ChatDataSource())
        viewModel = MessageViewModel(repository)
    }
}