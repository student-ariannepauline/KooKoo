package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.JoinedClass

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Firebase.firestore
    val joinedClasses: MutableLiveData<List<JoinedClass>> = MutableLiveData()


    fun joinClass(classId: String, uid: String) {
        db.collection("classes")
            .whereEqualTo("__name__", classId)
            .get()
            .addOnSuccessListener { documents ->
                val document = documents.first()
                val joinedClassesRef = db.collection("classes_joined").document(uid)

                joinedClassesRef.update(
                    "classes", FieldValue.arrayUnion(
                        mapOf(
                            "class_id" to classId,
                            "name" to document.data["name"]
                        )
                    )
                )
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
                joinClass(documentReference.id, uid)
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
                if (documents != null) {
                    val document = documents.first()
                    val classes = document.data["classes"] as List<*>
                    val joinedClassesResponse: MutableList<JoinedClass> = mutableListOf()

                    for (joinedClassMap in classes) {
                        joinedClassMap as Map<*, *>
                        val classId = joinedClassMap["class_id"]
                        val className = joinedClassMap["name"]
                        val joinedClass = JoinedClass(classId as String, className as String)

                        joinedClassesResponse += joinedClass

                    }

                    joinedClasses.value = joinedClassesResponse

                }
            }
    }
}