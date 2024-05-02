package com.fleaudie.chatapp.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.adapters.ChatAdapter
import com.fleaudie.chatapp.adapters.ContactsAdapter
import com.fleaudie.chatapp.data.model.Chat
import com.fleaudie.chatapp.data.model.Contact
import com.fleaudie.chatapp.databinding.FragmentChatsBinding
import com.fleaudie.chatapp.helpers.ChatHelper
import com.fleaudie.chatapp.helpers.ContactHelper
import com.fleaudie.chatapp.viewmodel.ChatViewModel
import com.fleaudie.chatapp.viewmodel.ContactViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatHelper = ChatHelper(requireContext().contentResolver)
        chatHelper.getContacts(
            onSuccess = { contactsList ->
                val registeredUsers = mutableListOf<Chat>()
                val unregisteredUsers = mutableListOf<Chat>()

                for (chat in contactsList) {
                    val formattedPhoneNumber = chat.phoneNumber.replace("\\s".toRegex(), "")
                    chatHelper.checkPhoneNumber(formattedPhoneNumber, onSuccess = { _ ->
                        registeredUsers.add(chat)
                        checkListCompletion(registeredUsers, unregisteredUsers, contactsList)
                    }, onFail = {
                        unregisteredUsers.add(chat)
                        checkListCompletion(registeredUsers, unregisteredUsers, contactsList)
                    })
                }
            },
            onFailure = { exception ->
                Log.e("chats", "Error getting contacts: $exception")
                Toast.makeText(requireContext(), "Error getting contacts", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun checkListCompletion(
        registeredUsers: MutableList<Chat>,
        unregisteredUsers: MutableList<Chat>,
        contactsList: List<Chat>
    ) {
        val mergedList = registeredUsers.sortedByDescending { it.name }

        val chatAdapter = ChatAdapter(mergedList, requireContext(), findNavController())
        binding.adapter = chatAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = false
        binding.rcyChatList.layoutManager = layoutManager
    }

}