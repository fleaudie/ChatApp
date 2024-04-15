package com.fleaudie.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.FragmentCodeBinding
import com.fleaudie.chatapp.viewmodel.CodeViewModel

class CodeFragment : Fragment() {

    // CodeFragment handles verification code input and navigation.
    private lateinit var codeViewModel: CodeViewModel
    private lateinit var binding: FragmentCodeBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_code ,container, false)
        binding.fragmentCode = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        // Initialize CodeViewModel.
        codeViewModel = ViewModelProvider(requireActivity())[CodeViewModel::class.java]

    }
    fun start(){
        val verifyCode = binding.editTextVerifyCode.text.toString()
        codeViewModel.verifyCode(verifyCode)

        view?.let { Navigation.findNavController(it).navigate(R.id.action_codeFragment_to_chatFragment) }
    }
}
