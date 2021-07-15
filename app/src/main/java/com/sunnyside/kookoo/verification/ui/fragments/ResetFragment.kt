package com.sunnyside.kookoo.verification.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentForgotpasswordBinding
import com.sunnyside.kookoo.databinding.FragmentProfileBinding
import com.sunnyside.kookoo.databinding.FragmentResetpasswordBinding

class ResetFragment : Fragment() {
    private var _binding: FragmentForgotpasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgotpasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViews()

        return view
    }

    private fun setupViews() {
        binding.createAccountTxt.setOnClickListener {
            findNavController().navigate(R.id.action_forgotFragment_to_signupFragment)
        }

        binding.forgotPasswordBtn.setOnClickListener {
            val email = binding.forgotPasswordTxt.text.toString()

            sendResetEmail(email)
        }
    }

    private fun sendResetEmail(email : String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("tite", "Email sent.")
                } else {
                    Log.d("tite", "Could not send email.")
                }
            }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}