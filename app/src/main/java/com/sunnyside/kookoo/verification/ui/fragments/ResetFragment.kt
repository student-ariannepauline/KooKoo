package com.sunnyside.kookoo.verification.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import kotlinx.android.synthetic.main.fragment_forgotpassword.*
import kotlinx.android.synthetic.main.fragment_forgotpassword.view.*


class ResetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgotpassword, container, false)

        view.forgotPassword_btn.setOnClickListener {
            forgotPassword()
        }

        view.create_account_txt.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_signupFragment)
        }

        view.login_back2.setOnClickListener {
            findNavController().navigate(R.id.action_forgotFragment_to_loginFragment)
        }

        return view
    }

    fun forgotPassword() {
        val emailAddress = forgotPassword_txt.text.toString()

        Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                findNavController().navigate(R.id.action_forgotFragment_to_loginFragment)
            }
        }
    }

}