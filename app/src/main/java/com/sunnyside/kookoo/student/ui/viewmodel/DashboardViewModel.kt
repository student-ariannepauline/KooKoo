package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.model.MemberModel

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Firebase.firestore
    val joinedClassesModel: MutableLiveData<List<JoinedClassModel>> = MutableLiveData()

    fun joinClass(classId: String, uid: String, is_admin: Boolean) {
        db.collection("classes")
            .whereEqualTo("__name__", classId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.first()

                    val classData = mapOf(
                        "class_id" to classId,
                        "name" to document.data["name"],
                        "is_admin" to is_admin
                    )

                    db.collection("classes_joined")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (!snapshot.exists()) {
                                val data = hashMapOf(
                                    "classes" to arrayListOf(classData)
                                )
                                db.collection("classes_joined").document(uid).set(data)
                            } else {
                                db.collection("classes_joined").document(uid).update(
                                    "classes", FieldValue.arrayUnion(classData)
                                )
                            }
                            subscribeToClass(classId)
                            beAMemberOfClass(classId, uid)
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
                Log.d("View Model", "DocumentSnapshot written with ID: ${documentReference.id}")
                joinClass(documentReference.id, uid, true)
            }
            .addOnFailureListener { e ->
                Log.w("View Model", "Error adding document", e)
            }
    }

    private fun getProfile(uid: String) : Task<QuerySnapshot> {
        val docRef = db.collection("user_profile").whereEqualTo("uid", uid)

        return docRef.get()
    }

    private fun beAMemberOfClass(classId : String, uid: String) {
        val profileQuery = getProfile(uid)
        val name = Firebase.auth.currentUser?.displayName as String

        profileQuery.addOnSuccessListener { profileDocuments ->

            if (profileDocuments != null && !profileDocuments.isEmpty) {
                val profile = profileDocuments.first()

                val level = profile["level"] as Long
                val program = profile["program"] as String
                val picLink = profile["picLink"] as String

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

        }
    }

    fun getClasses(uid: String): ListenerRegistration {
        val docRef = db.collection("classes_joined").whereEqualTo("__name__", uid)

        return docRef.addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w("View Model", "Listen failed.", e)
                return@addSnapshotListener
            }

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
                Log.d("View Model", joinedClassesModel.value.toString())

            } else {
                Log.d("View Model", "Current data: null")
            }

        }
    }

    private fun subscribeToClass(classID : String) {
        Firebase.messaging.subscribeToTopic(classID)
            .addOnCompleteListener { task ->
                var msg = "SUBSCRIBED to $classID"
                if (!task.isSuccessful) {
                    msg = "Can't SUBSCRIBED"
                }
                Log.d("View Model", msg)
            }
    }

    fun unsubscribeToClass(classID : String) {
        Firebase.messaging.unsubscribeFromTopic(classID)
            .addOnCompleteListener { task ->
                var msg = "UNSUBSCRIBED to $classID"
                if (!task.isSuccessful) {
                    msg = "Can't UNSUBSCRIBED"
                }
                Log.d("View Model", msg)
            }
    }
}