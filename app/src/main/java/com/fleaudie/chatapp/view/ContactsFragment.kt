package com.fleaudie.chatapp.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.adapters.ContactsAdapter
import com.fleaudie.chatapp.data.model.Contact
import com.fleaudie.chatapp.databinding.FragmentContactsBinding
import com.fleaudie.chatapp.helpers.ContactHelper
import com.fleaudie.chatapp.viewmodel.ContactViewModel
import com.fleaudie.chatapp.viewmodel.ContactViewModel.Companion.PERMISSIONS_REQUEST_CODE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {
    private lateinit var binding : FragmentContactsBinding
    private val contactViewModel: ContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)
        val anim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.root.startAnimation(anim)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (contactViewModel.checkPermissions(requireContext())) {
            val contactsHelper = ContactHelper(requireContext().contentResolver)
            val contactsList = contactsHelper.getContactsFromDevice()
            contactsHelper.saveContactsToFirestore()
            val registeredUsers = mutableListOf<Contact>()
            val unregisteredUsers = mutableListOf<Contact>()

            for (contact in contactsList) {
                val formattedPhoneNumber = contact.contactNumber.replace("\\s".toRegex(), "")
                contactsHelper.checkPhoneNumber(formattedPhoneNumber, onSuccess = { _ ->
                    registeredUsers.add(contact)
                    checkListCompletion(registeredUsers, unregisteredUsers, contactsList)
                }, onFail = {
                    unregisteredUsers.add(contact)
                    checkListCompletion(registeredUsers, unregisteredUsers, contactsList)
                })
            }
        } else {
            contactViewModel.requestPermissions(this)
        }
    }

    private fun checkListCompletion(
        registeredUsers: MutableList<Contact>,
        unregisteredUsers: MutableList<Contact>,
        contactsList: List<Contact>
    ) {
        if (registeredUsers.size + unregisteredUsers.size == contactsList.size) {
            registeredUsers.sortByDescending { it.contactName }
            unregisteredUsers.sortByDescending { it.contactName }

            val mergedList = mutableListOf<Contact>().apply {
                addAll(unregisteredUsers)
                addAll(registeredUsers)
            }

            val contactAdapter = ContactsAdapter(mergedList, requireContext())
            binding.adapterContact = contactAdapter

            val layoutManager = LinearLayoutManager(requireContext())
            layoutManager.reverseLayout = true
            binding.rcyContacts.layoutManager = layoutManager
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val contactsHelper = ContactHelper(requireContext().contentResolver)
                val contactsList = contactsHelper.getContactsFromDevice()
                contactsHelper.saveContactsToFirestore()

                val contactAdapter = ContactsAdapter(contactsList, requireContext())
                binding.adapterContact = contactAdapter
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission not granted for contacts!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}