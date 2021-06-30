package com.sunnyside.kookoo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.ui.StudentActivity
import com.sunnyside.kookoo.testKolanglods.ui.PangTestingLangLodsActivity
import com.sunnyside.kookoo.verification.ui.activity.VerificationActivity

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth

        checkForLoggedInUser()
    }

    private fun checkForLoggedInUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // intent = Intent(this, StudentActivity::class.java)
            intent = Intent(this, StudentActivity::class.java)

            startActivity(intent)
        } else {
            intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
        }
    }
}

fun Fragment.setAppBarTitle(title : String) {
    val my_activity = activity as AppCompatActivity?

    my_activity?.supportActionBar?.let { actionBar ->
        actionBar.title = title
    }
}

