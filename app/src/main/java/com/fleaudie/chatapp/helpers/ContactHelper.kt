package com.fleaudie.chatapp.helpers

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import com.fleaudie.chatapp.model.Contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ContactHelper(private val contentResolver: ContentResolver) {
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun saveContactsToFirestore() {
        currentUser?.let { user ->
            val contactsList = getContactsFromDevice()

            for (contact in contactsList) {
                val userContactsRef = firestore.collection("users").document(user.uid).collection("contacts")

                val contactData = hashMapOf(
                    "name" to contact.contactName,
                    "phoneNumber" to contact.contactNumber
                )

                userContactsRef.add(contactData)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error adding document", e)
                    }
            }
        }
    }

    @SuppressLint("Range")
    fun getContactsFromDevice(): List<Contact> {
        val contactsList = mutableListOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phones = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    phones?.use { phoneCursor ->
                        if (phoneCursor.moveToFirst()) {
                            val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contactsList.add(Contact(name, phoneNumber))
                        }
                    }
                } while (it.moveToNext())
            }
        }

        return contactsList
    }

    companion object {
        private const val TAG = "ContactsHelper"
    }
}