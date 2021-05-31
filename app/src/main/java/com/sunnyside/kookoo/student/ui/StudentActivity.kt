package com.sunnyside.kookoo.student.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.verification.ui.activity.VerificationActivity
import kotlinx.android.synthetic.main.activity_student.*

class StudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
    }
}