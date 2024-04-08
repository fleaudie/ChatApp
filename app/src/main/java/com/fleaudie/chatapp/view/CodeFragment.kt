package com.fleaudie.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.FragmentCodeBinding
import com.fleaudie.chatapp.viewmodel.SignUpViewModel

class CodeFragment : Fragment() {

    // CodeFragment handles verification code input and navigation.
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var binding: FragmentCodeBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize NavController for fragment navigation.
        navController = Navigation.findNavController(view)

        // Initialize SignUpViewModel.
        signUpViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        signUpViewModel.setActivity(requireActivity(), navController)
        signUpViewModel.init()

        // Set click listener for verification button.
        binding.btnStart.setOnClickListener{
            val verifyCode = binding.editTextVerifyCode.text.toString()
            signUpViewModel.verifyCode(verifyCode)

            Navigation.findNavController(it).navigate(R.id.action_codeFragment_to_chatFragment)
        }
    }
}
