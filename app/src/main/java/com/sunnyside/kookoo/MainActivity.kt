package com.sunnyside.kookoo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.sunnyside.kookoo.student.data.UserToken
import com.sunnyside.kookoo.student.ui.StudentActivity
import com.sunnyside.kookoo.verification.ui.activity.VerificationActivity

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth

        Firebase.messaging.subscribeToTopic("XyKnMkRPoCPcRRNDj0wR")
            .addOnCompleteListener { task ->
                var msg = "SUBSCRIBED to XyKnMkRPoCPcRRNDj0wR"
                if (!task.isSuccessful) {
                    msg = "Can't SUBSCRIBED"
                }
                Log.d("tite", msg)
            }

        checkForLoggedInUser()
    }

    private fun checkForLoggedInUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // intent = Intent(this, StudentActivity::class.java)
            intent = Intent(this, StudentActivity::class.java)

            currentUser.getIdToken(true).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val token = task.result?.token.toString()
                    UserToken.addToken(token)
                    Log.d("tite", token)

                } else {
                    Log.w("tite", "Error getting token", task.exception)
                }
            }

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

