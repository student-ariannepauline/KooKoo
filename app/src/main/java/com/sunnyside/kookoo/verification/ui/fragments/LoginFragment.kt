package com.sunnyside.kookoo.verification.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.verification.model.LoggedInUser
import com.sunnyside.kookoo.verification.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {

    private lateinit var mLoginViewModel: LoginViewModel
    private var loggedInUser = emptyList<LoggedInUser>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        view.textLoginRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        view.login_back.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
        }

        view.login_btn.setOnClickListener {
            login()
        }

        mLoginViewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {user ->
            loggedInUser            
        })
        
        Log.d("Test", loggedInUser.size.toString())

        return view
    }

    private fun login() {
        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()

        if (inputCheck(email, password)) {
            val loggedInUser = LoggedInUser(0, email, password)
            mLoginViewModel.login(loggedInUser)

            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(email: String, password: String): Boolean {
        return !(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
    }

}