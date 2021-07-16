package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.MemberModel

class MembersViewModel(application: Application) : AndroidViewModel(application)  {
    private val db = Firebase.firestore

    val membersList: MutableLiveData<ArrayList<MemberModel>> = MutableLiveData()

    fun getMembers(classId: String): ListenerRegistration {
        val docRef = db.collection("members_in_class").document(classId)

        return docRef.addSnapshotListener {documents, e ->
            if (e != null) {
                Log.w("View Model", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (documents != null && documents.exists()) {
                val members = documents["members"] as List<*>
                val memberResponseList = arrayListOf<MemberModel>()

                for (member in members) {
                    try {
                        member as Map<*, *>
                        val uid = member["uid"] as String
                        val name = member["name"] as String
                        val program = member["program"] as String
                        val level = member["level"] as Long
                        val picLink = member["picLink"] as String

                        val joinedMember = MemberModel(
                            uid,
                            name,
                            program,
                            level,
                            picLink
                        )

                        memberResponseList += joinedMember
                    }
                    catch (e: Exception) {

                    }
                }

                membersList.value = memberResponseList
            } else {
                Log.d("View Model", "No data available")
            }
        }
    }

}

