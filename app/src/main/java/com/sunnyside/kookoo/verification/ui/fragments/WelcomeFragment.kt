package com.sunnyside.kookoo.verification.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sunnyside.kookoo.R
import kotlinx.android.synthetic.main.fragment_welcome.view.*

class WelcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        view.welcome_login_btn.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        view.welcome_signup_btn.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_signupFragment)
        }

        return view
    }

}