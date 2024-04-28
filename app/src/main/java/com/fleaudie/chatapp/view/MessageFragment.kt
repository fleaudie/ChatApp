package com.fleaudie.chatapp.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.adapters.MessageAdapter
import com.fleaudie.chatapp.data.datasource.ChatDataSource
import com.fleaudie.chatapp.data.repository.ChatRepository
import com.fleaudie.chatapp.databinding.FragmentMessageBinding
import com.fleaudie.chatapp.databinding.PopupProfileDetailBinding
import com.fleaudie.chatapp.viewmodel.MessageViewModel
import com.google.firebase.auth.FirebaseAuth

class MessageFragment : Fragment() {
    private lateinit var binding : FragmentMessageBinding
    private lateinit var name: String
    private lateinit var adapter: MessageAdapter
    private lateinit var viewModel: MessageViewModel
    private lateinit var receiverId: String
    private lateinit var text: String
    private lateinit var phoneNumber: String
    private var popupWindow: PopupWindow? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)
        binding.msgProfileDetail = this
        val args: MessageFragmentArgs by navArgs()
        name = args.name
        receiverId = args.uid
        phoneNumber = args.number
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtMsgContactName.text = name

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        adapter = MessageAdapter(emptyList(), requireContext())
        binding.rcyMessages.adapter = adapter

        binding.rcyMessages.layoutManager = LinearLayoutManager(requireContext())
        viewModel.messageList.observe(viewLifecycleOwner) {
            adapter.updateMessages(it)
            binding.rcyMessages.scrollToPosition(it.size - 1)
        }
        binding.floatSendMessage.setOnClickListener {
            text = binding.editTextMessage.text.toString()
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

    fun msgProfileDetail(){
        showPopup()
    }

    private fun showPopup() {
        val inflater = LayoutInflater.from(context)
        val popupBinding = PopupProfileDetailBinding.inflate(inflater, null, false)
        val view = popupBinding.root

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels * 0.8).toInt()

        popupWindow = PopupWindow(
            view,
            width,
            LinearLayout.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow?.showAtLocation(view, Gravity.END, 0, 0)

        val upwardSlideAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left)
        view.startAnimation(upwardSlideAnimation)

        val number = phoneNumber.replace("\\s".toRegex(), "")
        viewModel.getProfileImageUrls(
            phoneNumber = number,
            onSuccess = { imageUrls ->
                if (imageUrls.isNotEmpty()) {
                    Glide.with(requireContext())
                        .load(imageUrls.values.first())
                        .into(popupBinding.imgMsgProfilePhoto)
                } else {
                    popupBinding.imgMsgProfilePhoto.setImageResource(R.drawable.empty_profile_image)
                }
            },
            onFailure = {
                Toast.makeText(requireContext(), "Error install user detail.", Toast.LENGTH_SHORT).show()
            }
        )

        popupBinding.txtReceiverName.text = name
        popupBinding.txtReceiverNumber.text = number
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = ChatRepository(ChatDataSource())
        viewModel = MessageViewModel(repository)
    }


}