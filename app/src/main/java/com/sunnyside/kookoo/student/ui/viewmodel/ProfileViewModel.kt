package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.StudentProfileModel
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application): AndroidViewModel(application) {
    val userProfileModel: MutableLiveData<StudentProfileModel> = MutableLiveData()
    private val db = Firebase.firestore

    fun getProfile(uid:String){
        viewModelScope.launch {
            db.collection("user_profile")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { documents ->
                    val document = documents.first()
                    val timestamp = document.data["birthDate"] as com.google.firebase.Timestamp
                    val date = timestamp.toDate()
                    val level = document.data["level"] as Long

                    userProfileModel.value = StudentProfileModel(
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