package com.fleaudie.chatapp.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.adapters.ContactsAdapter
import com.fleaudie.chatapp.databinding.FragmentContactsBinding
import com.fleaudie.chatapp.helpers.ContactHelper
import com.fleaudie.chatapp.viewmodel.ContactViewModel
import com.fleaudie.chatapp.viewmodel.ContactViewModel.Companion.PERMISSIONS_REQUEST_CODE
import com.fleaudie.chatapp.viewmodel.SignInViewModel

class ContactsFragment : Fragment() {
    private lateinit var binding : FragmentContactsBinding
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactViewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        if (contactViewModel.checkPermissions(requireContext())) {
            val contactsHelper = ContactHelper(requireContext().contentResolver)
            val contactsList = contactsHelper.getContactsFromDevice()
            contactsHelper.saveContactsToFirestore()

            val contactAdapter = ContactsAdapter(contactsList, requireContext())
            binding.adapterContact = contactAdapter
        } else {
            contactViewModel.requestPermissions(this)
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
                    "Kişilere erişim izni verilmedi!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}