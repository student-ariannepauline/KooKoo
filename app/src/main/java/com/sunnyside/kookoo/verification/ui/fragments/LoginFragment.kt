package com.sunnyside.kookoo.verification.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.student.ui.StudentActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        auth = Firebase.auth


        view.textLoginRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        view.loginbtnBack.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
        }

        view.reset_password_txt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetFragment)
        }

        view.login_btn.setOnClickListener {
            login()
        }


        return view
    }

    private fun login() {
        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTH", "signInWithEmail:success")
                    val user = auth.currentUser

                    val intent = Intent(activity, StudentActivity::class.java)
                    activity?.startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AUTH",  "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun inputCheck(email: String, password: String): Boolean {
        return !(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
    }

}