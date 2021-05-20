package com.sunnyside.kookoo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyside.kookoo.student.ui.StudentActivity
import com.sunnyside.kookoo.verification.ui.activity.VerificationActivity
import com.sunnyside.kookoo.verification.viewmodel.LoginViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        checkForLoggedInUser()
    }

    private fun checkForLoggedInUser() {
        mLoginViewModel.checkForUser()
        var intent: Intent

        mLoginViewModel.loggedInUser.observe(this, Observer { user ->
            if (user != null) {
                intent = Intent(this, StudentActivity::class.java)
            } else {
                intent = Intent(this, VerificationActivity::class.java)
            }
            startActivity(intent)
        })
    }
}