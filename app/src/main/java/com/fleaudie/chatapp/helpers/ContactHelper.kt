package com.fleaudie.chatapp.helpers

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import com.fleaudie.chatapp.data.model.Contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ContactHelper(private val contentResolver: ContentResolver) {
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    fun saveContactsToFirestore() {
        currentUser?.let { user ->
            val contactsList = getContactsFromDevice()

            val userContactsRef = firestore.collection("users").document(user.uid).collection("contacts")
            userContactsRef.get()
                .addOnSuccessListener { documents ->
                    val existingContacts = HashSet<String>()
                    for (document in documents) {
                        val phoneNumber = document.getString("phoneNumber")
                        phoneNumber?.let { existingContacts.add(it) }
                    }

                    for (contact in contactsList) {
                        if (!existingContacts.contains(contact.contactNumber)) {
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
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error getting existing contacts", e)
                }
        }
    }

    fun checkPhoneNumber(phoneNumber: String, onSuccess: (uid: String?) -> Unit, onFail: () -> Unit) {
        db.collection("users")
            .whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val uid = documents.documents[0].id
                    onSuccess(uid)
                } else {
                    onFail()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error checking phone number", e)
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