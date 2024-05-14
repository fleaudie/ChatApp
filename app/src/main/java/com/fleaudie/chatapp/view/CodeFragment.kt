package com.fleaudie.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.FragmentCodeBinding
import com.fleaudie.chatapp.viewmodel.CodeViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodeFragment : Fragment() {
    private lateinit var binding: FragmentCodeBinding
    private val viewModel: CodeViewModel by viewModels()
    private lateinit var verificationId: String
    private lateinit var name: String
    private lateinit var surname: String
    private lateinit var phoneNumber: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code, container, false)
        binding.fragmentCode = this

        val args: CodeFragmentArgs by navArgs()
            verificationId = args.verificationId
            name = args.name
            surname = args.surname
            phoneNumber = args.phoneNumber


        viewModel.verificationResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_codeFragment_to_chatFragment)
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    viewModel.writeUserData(phoneNumber, uid, name, surname)
                }
            } else {
                view?.let { Snackbar.make(it, "Wrong Otp!", Snackbar.LENGTH_SHORT).show() }
            }
        }
        return binding.root
    }

    fun start(){
        val otpCode = binding.editTextVerifyCode.text.toString()
        viewModel.verifyOtp(verificationId, otpCode)
    }
}
