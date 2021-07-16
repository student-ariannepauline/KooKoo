package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.StudentProfileModel
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    val userProfileModel: MutableLiveData<StudentProfileModel> = MutableLiveData()
    private val db = Firebase.firestore

    fun getProfile(uid: String): ListenerRegistration {
        val docRef = db.collection("user_profile").whereEqualTo("uid", uid)

        return docRef.addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w("View Model", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (documents != null && !documents.isEmpty) {
                val document = documents.first()
                val timestamp = document.data["birthDate"] as com.google.firebase.Timestamp
                val date = timestamp.toDate()
                val level = document.data["level"] as Long

                userProfileModel.value = StudentProfileModel(
                    document.data["uid"] as String,
                    document.data["firstName"] as String,
                    document.data["lastName"] as String,
                    document.data["contactNumber"] as String,
                    document.data["address"] as String,
                    document.data["email"] as String,
                    date,
                    document.data["program"] as String,
                    level,
                    document.data["picLink"] as String
                )
            }

        }
    }
}