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

        val user = Firebase.auth.currentUser

        user?.let {
            user_email_txt.text = user.email
            user_id_txt.text = user.uid
            user_name_txt.text = user.displayName
        }


        log_out_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}