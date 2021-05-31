package com.sunnyside.kookoo.student.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.StudentProfile
import java.text.SimpleDateFormat

class ProfileRepository {
    private val db = Firebase.firestore

    var sfd: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    fun getProfile(uid: String): StudentProfile {
        var profile = StudentProfile()
        db.collection("user_profile")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { documents ->
                val document = documents.first()
                val timestamp = document.data["birthDate"] as com.google.firebase.Timestamp
                val date = timestamp.toDate()
                val level = document.data["level"] as Long

                profile = StudentProfile(
                    document.data["uid"].toString(),
                    document.data["firstName"].toString(),
                    document.data["lastName"].toString(),
                    document.data["contactNumber"].toString(),
                    document.data["address"].toString(),
                    document.data["email"].toString(),
                    date,
                    document.data["program"].toString(),
                    level,
                )

            }

        return profile
    }
}