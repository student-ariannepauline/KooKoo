package com.sunnyside.kookoo.verification.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*

class SignupFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        auth = Firebase.auth

        view.btnSignup.setOnClickListener {
            signup()
        }
        return view
    }

    private fun signup() {
        val email = signupEmail.text.toString()
        val password = signupPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTH", "createUserWithEmail:success")

                    val user = Firebase.auth.currentUser

                    val profileUpdates = userProfileChangeRequest {
                        displayName = signupName.text.toString()
                    }

                    user!!.updateProfile(profileUpdates).addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            findNavController().navigate(R.id.action_signupFragment_to_welcomeFragment)
                        }
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AUTH", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}