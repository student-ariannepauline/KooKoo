package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.StudentProfileModel

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
    val userProfileModel: MutableLiveData<StudentProfileModel> = MutableLiveData()
    private val db = Firebase.firestore

    fun saveProfile(editedProfile: StudentProfileModel) {
        val docRef = db.collection("user_profile").whereEqualTo("uid", editedProfile.uid)

        val user = Firebase.auth.currentUser

        user!!.updateEmail(editedProfile.email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("tite", "User email address updated.")
                }
            }

        val profileData = mapOf(
            "uid" to editedProfile.uid,
            "firstName" to editedProfile.firstName,
            "lastName" to editedProfile.lastName,
            "contactNumber" to editedProfile.contactNumber,
            "address" to editedProfile.address,
            "email" to editedProfile.email,
            "level" to editedProfile.level,
            "program" to editedProfile.program,
            "birthDate" to Timestamp(editedProfile.birthDate),
            "picLink" to editedProfile.picLink,
        )

        docRef.get()
            .addOnSuccessListener { documents ->
                if (documents != null && !documents.isEmpty) {
                    val document = documents.first()

                    db.collection("user_profile").document(document.id).set(profileData)
                        .addOnSuccessListener {
                            Log.d("tite", "Profile Updated")
                        }
                        .addOnFailureListener {
                            Log.d("tite", "Profile Not Saved")
                        }

                }
            }

    }
}