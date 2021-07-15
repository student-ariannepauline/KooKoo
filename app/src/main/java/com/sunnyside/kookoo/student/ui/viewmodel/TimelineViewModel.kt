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
import java.time.format.DateTimeFormatter

class TimelineViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore
    val announcements: MutableLiveData<ArrayList<AnnouncementModel>> = MutableLiveData()

    fun deleteAnnouncement(documentId: String) {
        db.collection("announcements").document(documentId)

        val docRef = db.collection("announcements").document(documentId)

        docRef.get()
            .addOnSuccessListener { document ->
                val deadline = document["deadline"] as Timestamp
                val deadlineLocalDate =
                    deadline.toDate().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate()
                val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")

                val dateId = deadlineLocalDate.format(formatter)
                val classId = document["class_id"] as String

                val eventsRef = db.collection("events")
                    .whereEqualTo("class_id", classId)
                    .whereEqualTo("date", dateId)

                eventsRef.get()
                    .addOnSuccessListener { eventsDocuments ->
                        if (eventsDocuments != null && !eventsDocuments.isEmpty) {
                            val eventDocument = eventsDocuments.first()
                            val events = eventDocument["events"] as ArrayList<*>

                            val iter = events.iterator()

                            while(iter.hasNext()) {
                                val event = iter.next() as Map<*,*>
                                val eventData = event["data"] as Map<*, *>
                                val eventId = eventData["id"]

                                if (eventId == documentId) {
                                    iter.remove()
                                }
                            }

                            db.collection("events").document(eventDocument.id)
                                .update("events", events)
                                .addOnSuccessListener {
                                    Log.d("tite", "DocumentSnapshot successfully event!")
                                }
                                .addOnFailureListener {
                                        e -> Log.w("tite", "Error deleting document", e)
                                }
                        }
                    }

                docRef.delete()
                    .addOnSuccessListener { Log.d("tite", "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w("tite", "Error deleting document", e) }
            }
    }

    fun getAnnouncements(classId: String): ListenerRegistration {
        val docRef = db.collection("announcements").whereEqualTo("class_id", classId)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        return docRef.addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w("tite", "Listen failed.", e)
                return@addSnapshotListener
            }


            if (documents != null && !documents.isEmpty) {
                val announcementListResponse = ArrayList<AnnouncementModel>()

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
                        document.id,
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
    }

}