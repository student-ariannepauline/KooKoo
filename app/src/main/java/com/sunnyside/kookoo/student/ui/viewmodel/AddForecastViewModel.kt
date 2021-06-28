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

class AddForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore

    fun addForecast(forecast: ForecastModel, classId: String) {
        val meetingTime = Date.from(forecast.meeting_time.toInstant(ZoneOffset.ofHours(8)))
        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        val date = forecast.meeting_time.format(formatter)

        val newForecast = hashMapOf(
            "class_id" to classId,
            "title" to forecast.title,
            "link" to forecast.link,
            "status" to forecast.status,
            "meeting_time" to Timestamp(meetingTime)
        )

        db.collection("forecasts")
            .add(newForecast)
            .addOnSuccessListener { documentReference ->
                Log.d("tite", "Document with id: ${documentReference.id} successfully added")

                val event = mapOf(
                    "type" to "forecast",
                    "data" to mapOf(
                        "id" to documentReference.id,
                        "title" to forecast.title,
                        "status" to forecast.status,
                    )
                )

                val eventsRef = db.collection("events")
                    .whereEqualTo("class_id", classId)
                    .whereEqualTo("date", date)

                eventsRef
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (!snapshot.isEmpty) {
                            val documentEvent = snapshot.first()
                            db.collection("events").document(documentEvent.id).update(
                                "events", FieldValue.arrayUnion(event)
                            )
                        } else {
                            val data = hashMapOf(
                                "class_id" to classId,
                                "date" to date,
                                "events" to arrayListOf(event)
                            )

                            db.collection("events").add(data)
                        }
                    }

            }
            .addOnFailureListener { e ->
                Log.w("tite", "Error adding document", e)
            }


    }
}