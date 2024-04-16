package com.fleaudie.chatapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.FragmentSignUpBinding
import com.fleaudie.chatapp.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {

    // SignUpFragment handles user sign-up UI and interactions.
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.fragmentSignUp = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize NavController
        navController = Navigation.findNavController(requireView())

        // Initialize SignUpViewModel.
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        signUpViewModel.setActivity(requireActivity(), navController)
        signUpViewModel.init()
    }

    fun signIn(){
        view?.let { Navigation.findNavController(it).navigate(R.id.action_signUpFragment_to_signInFragment) }
    }

    fun signUp(){
        val countryCode = binding.countyCodePicker.selectedCountryCode
        val number = binding.editTextPhone.text.toString()
        val phNumber = "+$countryCode$number"

        val name = binding.editTextName.text.toString()
        val surname = binding.editTextSurname.text.toString()

        Log.d("SignUpFragment", "Phone number: $phNumber")
        signUpViewModel.signUp(phNumber)
        signUpViewModel.addFirestore(name,surname,phNumber)

    }

    override fun onResume() {
        super.onResume()
        signUpViewModel.checkIfUserLoggedIn()
    }
}
