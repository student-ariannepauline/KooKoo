package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.JoinedClassModel

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Firebase.firestore
    val joinedClassesModel: MutableLiveData<List<JoinedClassModel>> = MutableLiveData()

    fun joinClass(classId: String, uid: String, is_admin: Boolean) {
        db.collection("classes")
            .whereEqualTo("__name__", classId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    Log.d("tite", documents.toString())
                    val document = documents.first()
                    Log.d("tite", "Nakita ko yung class")

                    db.collection("classes_joined")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (!snapshot.exists()) {
                                Log.d("tite", "SHT")
                                val data = hashMapOf(
                                    "classes" to arrayListOf(mapOf(
                                        "class_id" to classId,
                                        "name" to document.data["name"],
                                        "is_admin" to is_admin
                                    ))
                                )
                                db.collection("classes_joined").document(uid).set(data)
                            } else {
                                Log.d("tite", "Tite")
                                db.collection("classes_joined").document(uid).update(
                                    "classes", FieldValue.arrayUnion(
                                        mapOf(
                                            "class_id" to classId,
                                            "name" to document.data["name"],
                                            "is_admin" to is_admin
                                        )
                                    )
                                )
                            }
                        }
                }
            }
    }

    fun createClass(class_name: String, uid: String) {
        val newClass = hashMapOf(
            "name" to class_name
        )
        db.collection("classes")
            .add(newClass)
            .addOnSuccessListener { documentReference ->
                Log.d("tite", "DocumentSnapshot written with ID: ${documentReference.id}")
                joinClass(documentReference.id, uid, true)
            }
            .addOnFailureListener { e ->
                Log.w("tite", "Error adding document", e)
            }
    }

    fun getClasses(uid: String) {
        db.collection("classes_joined")
            .whereEqualTo("__name__", uid)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.first()
                    val classes = document.data["classes"] as List<*>
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
                    Log.d("Tite", joinedClassesModel.value.toString())

                }
            }
    }
}