package com.fleaudie.chatapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.datasource.AuthDataSource
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.fleaudie.chatapp.databinding.FragmentSignUpBinding
import com.fleaudie.chatapp.viewmodel.SignUpViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private lateinit var viewModel : SignUpViewModel
    private lateinit var navController: NavController
    private var user = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.fragmentSignUp = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    fun signUp(){
        val countryCode = binding.countyCodePicker.selectedCountryCode.toString()
        val number = binding.editTextPhone.text.toString()
        val name = binding.editTextName.text.toString()
        val surname = binding.editTextSurname.text.toString()
        val phoneNumber = "+$countryCode$number"

        if(name.isBlank() || surname.isBlank() || number.isBlank()){
            Snackbar.make(requireView(), "Please fill in the blank fields!", Snackbar.LENGTH_SHORT).show()
        }else{
            viewModel.sendVerificationCode(this, phoneNumber, name, surname)
            Log.d("SignUpFragment", "Phone number: $phoneNumber")
        }

    }

    override fun onStart() {
        super.onStart()
        if (user != null){
            navController.navigate(R.id.action_signUpFragment_to_chatFragment)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authRepository = AuthRepository(AuthDataSource(requireActivity()))
        viewModel = SignUpViewModel(authRepository)
    }
}
