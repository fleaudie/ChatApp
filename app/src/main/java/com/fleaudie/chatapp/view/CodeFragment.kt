package com.fleaudie.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.datasource.AuthDataSource
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.fleaudie.chatapp.databinding.FragmentCodeBinding
import com.fleaudie.chatapp.viewmodel.CodeViewModel
import com.google.android.material.snackbar.Snackbar

class CodeFragment : Fragment() {
    private lateinit var binding: FragmentCodeBinding
    private lateinit var viewModel: CodeViewModel
    private lateinit var verificationId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code, container, false)
        binding.fragmentCode = this

        arguments?.let {
            verificationId = CodeFragmentArgs.fromBundle(it).verificationId
        }

        viewModel.verificationResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_codeFragment_to_chatFragment)
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authRepository = AuthRepository(AuthDataSource(requireContext()))
        viewModel = CodeViewModel(authRepository)
    }

}
