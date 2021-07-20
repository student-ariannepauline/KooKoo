package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.model.MemberModel
import com.sunnyside.kookoo.student.model.StudentProfileModel

class CreateProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore
    private val joinedClassesModel: MutableLiveData<List<JoinedClassModel>> = MutableLiveData()

    fun setProfile(newProfile : StudentProfileModel) {
        val docRef = db.collection("user_profile")

        val profileData = mapOf(
            "uid" to newProfile.uid,
            "firstName" to newProfile.firstName,
            "lastName" to newProfile.lastName,
            "contactNumber" to newProfile.contactNumber,
            "address" to newProfile.address,
            "email" to newProfile.email,
            "level" to newProfile.level,
            "program" to newProfile.program,
            "birthDate" to Timestamp(newProfile.birthDate),
            "picLink" to newProfile.picLink,
        )

        db.collection("user_profile").whereEqualTo("uid", newProfile.uid)
            .get()
            .addOnSuccessListener { documents ->
                if(documents.isEmpty) {
                    docRef.add(profileData)
                        .addOnSuccessListener {
                            getClasses(newProfile.uid, newProfile)

                        }
                }
            }

    }

    private fun beAMemberOfClass(newProfile : StudentProfileModel, classId : String) {
        val uid = newProfile.uid
        val name = newProfile.firstName
        val level = newProfile.level
        val program = newProfile.program
        val picLink = newProfile.picLink

        val newMember = MemberModel(
            uid,
            name,
            program,
            level,
            picLink
        )

        db.collection("members_in_class")
            .document(classId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    val data = hashMapOf(
                        "members" to arrayListOf(newMember)
                    )
                    db.collection("members_in_class").document(classId).set(data)
                }
                else {
                    db.collection("members_in_class").document(classId).update(
                        "members", FieldValue.arrayUnion(newMember)
                    )
                }
            }
    }

    fun getClasses(uid: String, newProfile : StudentProfileModel){
        val docRef = db.collection("classes_joined").whereEqualTo("__name__", uid)

        docRef.get()
            .addOnSuccessListener{ documents ->
            if (documents != null && !documents.isEmpty) {
                val classes = documents.first().data["classes"] as List<*>
                val joinedClassesResponseModel: MutableList<JoinedClassModel> = mutableListOf()

                for (joinedClassMap in classes) {
                    joinedClassMap as Map<*, *>
                    val classId = joinedClassMap["class_id"]
                    val className = joinedClassMap["name"]
                    val isAdmin = joinedClassMap["is_admin"]
                    val joinedClass = JoinedClassModel(
                        classId as String,
                        className as String,
                        isAdmin as Boolean
                    )

                    joinedClassesResponseModel += joinedClass
                }

                joinedClassesModel.value = joinedClassesResponseModel

                for (joinedClass in joinedClassesModel.value!!) {
                    beAMemberOfClass(newProfile, joinedClass.class_id)
                }

                Log.d("View Model", joinedClassesModel.value.toString())

            } else {
                Log.d("View Model", "Current data: null")
            }

        }
    }

}