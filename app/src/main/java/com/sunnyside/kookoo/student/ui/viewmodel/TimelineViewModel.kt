package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.core.OrderBy
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.JoinedClassModel
import java.time.ZoneId

class TimelineViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore
    val announcements: MutableLiveData<List<AnnouncementModel>> = MutableLiveData()

    fun getAnnouncements(classId: String) : ListenerRegistration{
        val docRef = db.collection("announcements").whereEqualTo("class_id", classId).orderBy("timestamp", Query.Direction.DESCENDING)

        return docRef.addSnapshotListener {documents, e->
                if (e != null) {
                    Log.w("tite", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (documents != null && !documents.isEmpty) {
                    val announcementListResponse : MutableList<AnnouncementModel> = mutableListOf()

                    for (document in documents) {
                        val authorName = document["author_name"] as String
                        val picLink = document["pic_link"] as String
                        val title = document["title"] as String
                        val body = document["body"] as String
                        val link = document["link"] as String
                        val deadline = document["deadline"] as Timestamp
                        val timestamp = document["timestamp"] as Timestamp

                        val deadlineLocalDate =
                            deadline.toDate().toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        val timestampLocalDateTime =
                            timestamp.toDate().toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDateTime()

                        val announcement = AnnouncementModel(
                            authorName,
                            picLink,
                            title,
                            body,
                            link,
                            deadlineLocalDate,
                            timestampLocalDateTime
                        )

                        announcementListResponse += announcement
                    }

                    announcements.value = announcementListResponse
                }
            }

        /*db.collection("announcements")
            .whereEqualTo("class_id", classId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {

                    val announcementListResponse : MutableList<AnnouncementModel> = mutableListOf()

                    for (document in documents) {
                        document.data as Map<*, *>
                        val authorName = document["author_name"] as String
                        val picLink = document["pic_link"] as String
                        val title = document["title"] as String
                        val body = document["body"] as String
                        val link = document["link"] as String
                        val deadline = document["deadline"] as Timestamp
                        val timestamp = document["timestamp"] as Timestamp

                        val deadlineLocalDate =
                            deadline.toDate().toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        val timestampLocalDateTime =
                            timestamp.toDate().toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDateTime()

                        val announcement = AnnouncementModel(
                            authorName,
                            picLink,
                            title,
                            body,
                            link,
                            deadlineLocalDate,
                            timestampLocalDateTime
                        )

                        announcementListResponse += announcement
                    }

                    announcements.value = announcementListResponse
                }
            }*/
    }

}