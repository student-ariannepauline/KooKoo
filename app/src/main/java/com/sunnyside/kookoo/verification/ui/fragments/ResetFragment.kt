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
import kotlinx.android.synthetic.main.fragment_reset.*
import kotlinx.android.synthetic.main.fragment_reset.view.*


class ResetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reset, container, false)

        view.reset_btn.setOnClickListener {
            resetPassword()
        }

        return view
    }

    fun resetPassword() {
        val emailAddress = reset_email_txt.text.toString()

        Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                findNavController().navigate(R.id.action_resetFragment_to_loginFragment)
            }
        }
    }

}