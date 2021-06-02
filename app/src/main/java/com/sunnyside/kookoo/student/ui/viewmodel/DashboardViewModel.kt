package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.data.repository.ProfileRepository
import com.sunnyside.kookoo.student.model.StudentProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application): AndroidViewModel(application) {

    private val profileRepository: ProfileRepository = ProfileRepository()
    val userProfile: MutableLiveData<StudentProfile> = MutableLiveData()
    private val db = Firebase.firestore

    fun getProfile(uid:String){
        viewModelScope.launch {
            db.collection("user_profile")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Log.d("Tite", "Walang laman lods")
                    }

                    else {
                        val document = documents.first()
                        val timestamp = document.data["birthDate"] as com.google.firebase.Timestamp
                        val date = timestamp.toDate()
                        val level = document.data["level"] as Long

                        userProfile.value = StudentProfile(
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

                }
        }
    }
}