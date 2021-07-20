package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.ForecastModel
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class EditForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore

    fun saveForecast(editedForecast: ForecastModel, classId: String, oldForecast: ForecastModel,) {
        val meetingTime = Date.from(editedForecast.meeting_time.toInstant(ZoneOffset.ofHours(8)))

        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        val dateId = editedForecast.meeting_time.format(formatter)
        val oldDateId = oldForecast.meeting_time.format(formatter)

        val newForecast = hashMapOf(
            "class_id" to classId,
            "title" to editedForecast.title,
            "link" to editedForecast.link,
            "status" to editedForecast.status,
            "meeting_time" to Timestamp(meetingTime)
        )

        val announcementRef = db.collection("forecasts").document(editedForecast.documentID)

        announcementRef
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    announcementRef.set(newForecast)
                        .addOnSuccessListener {
                            saveForecastEvent(dateId, editedForecast, classId, oldDateId)
                        }
                }
            }



    }


    private fun saveForecastEvent(dateId: String, editedForecast: ForecastModel, classId: String, oldDateId: String) {
        val newEvent = mapOf(
            "type" to "forecast",
            "data" to mapOf(
                "id" to editedForecast.documentID,
                "title" to editedForecast.title,
                "status" to editedForecast.status,
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

                        if (eventId == editedForecast.documentID) {
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