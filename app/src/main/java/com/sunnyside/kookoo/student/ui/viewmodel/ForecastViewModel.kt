package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.ForecastModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore
    val forecasts: MutableLiveData<ArrayList<ForecastModel>> = MutableLiveData()

    fun deleteForecast(documentId : String) {
        val docRef = db.collection("forecasts").document(documentId)

        docRef.get()
            .addOnSuccessListener { document ->
                val meetingTime = document["meeting_time"] as Timestamp

                val meetingTimeLocalDateTime =
                    meetingTime.toDate().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDateTime()

                val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")

                val dateId = meetingTimeLocalDateTime.format(formatter)
                val classId = document["class_id"]

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
                                    Log.d("View Model", "DocumentSnapshot successfully event!")
                                }
                                .addOnFailureListener {
                                        e -> Log.w("View Model", "Error deleting document", e)
                                }

                        }
                    }

                docRef.delete()
                    .addOnSuccessListener { Log.d("View Model", "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w("View Model", "Error deleting document", e) }
            }
    }


    fun getForecasts(classId: String): ListenerRegistration {
        val docRef = db.collection("forecasts").whereEqualTo("class_id", classId)
            .orderBy("meeting_time", Query.Direction.DESCENDING)

        return docRef.addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w("View Model", "Listen failed", e)
                return@addSnapshotListener
            }

            if (documents != null && !documents.isEmpty) {
                val forecastListResponse = ArrayList<ForecastModel>()

                for (document in documents) {
                    val title = document["title"] as String
                    val link = document["link"] as String
                    val status = document["status"] as String
                    val meetingTime = document["meeting_time"] as Timestamp

                    val meetingTimeLocalDateTime =
                        meetingTime.toDate().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDateTime()

                    val forecast = ForecastModel(
                        document.id,
                        title,
                        link,
                        status,
                        meetingTimeLocalDateTime
                    )

                    forecastListResponse += forecast
                }

                forecasts.value = forecastListResponse
            }
        }
    }
}