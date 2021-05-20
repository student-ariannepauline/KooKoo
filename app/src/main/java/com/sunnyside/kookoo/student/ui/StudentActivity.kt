package com.sunnyside.kookoo.student.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.verification.ui.activity.VerificationActivity
import com.sunnyside.kookoo.verification.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_student.*

class StudentActivity : AppCompatActivity() {
    private lateinit var mLoginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        log_out_btn.setOnClickListener {
            mLoginViewModel.logout()
            intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}