package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.ForecastModel
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class EditPostViewModel(application: Application) : AndroidViewModel(application)  {
    private val db = Firebase.firestore

    fun savePost(editedPost: AnnouncementModel, classId: String, oldPost: AnnouncementModel) {
        val date = Date.from(editedPost.deadline.atStartOfDay().toInstant(ZoneOffset.UTC))
        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        val dateId = editedPost.deadline.format(formatter)
        val oldDateId = oldPost.deadline.format(formatter)

        val newPost = hashMapOf(
            "class_id" to classId,
            "author_name" to editedPost.author_name,
            "pic_link" to editedPost.pic_link,
            "title" to editedPost.title,
            "body" to editedPost.body,
            "link" to editedPost.link,
            "deadline" to Timestamp(date),
            "timestamp" to Timestamp.now()
        )

        val announcementRef = db.collection("announcements").document(editedPost.documentID)

        announcementRef
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    announcementRef.set(newPost)
                        .addOnSuccessListener {
                            savePostEvent(dateId, editedPost, classId, oldDateId)
                        }
                }
            }
    }

    private fun savePostEvent(dateId: String, editedPost: AnnouncementModel, classId: String, oldDateId: String) {
        val newEvent = mapOf(
            "type" to "announcement",
            "data" to mapOf(
                "id" to editedPost.documentID,
                "title" to editedPost.title,
            )
        )

        val oldEventsRef = db.collection("events")
            .whereEqualTo("class_id", classId)
            .whereEqualTo("date", oldDateId)

        val newEventsRef = db.collection("events")
            .whereEqualTo("class_id", classId)
            .whereEqualTo("date", dateId)

        oldEventsRef.get()
            .addOnSuccessListener { eventsDocuments ->
                if (eventsDocuments != null && !eventsDocuments.isEmpty) {
                    val eventDocument = eventsDocuments.first()
                    val events = eventDocument["events"] as MutableList<*>

                    val iter = events.listIterator()

                    while(iter.hasNext()) {
                        val event = iter.next() as Map<*,*>
                        val eventData = event["data"] as Map<*, *>
                        val eventId = eventData["id"]

                        if (eventId == editedPost.documentID) {
                            iter.remove()
                        }
                    }

                    db.collection("events").document(eventDocument.id)
                        .update("events", events)
                        .addOnSuccessListener {
                            Log.d("View Model", "DocumentSnapshot successfully event!")
                        }
                        .addOnFailureListener {
                                e -> Log.w("View Model", "Error deleting document", e)
                        }

                }
            }

        newEventsRef
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val documentEvent = snapshot.first()
                    db.collection("events").document(documentEvent.id).update(
                        "events", FieldValue.arrayUnion(newEvent)
                    )
                } else {
                    val data = hashMapOf(
                        "class_id" to classId,
                        "date" to dateId,
                        "events" to arrayListOf(newEvent)
                    )

                    db.collection("events").add(data)
                }
            }
    }

}