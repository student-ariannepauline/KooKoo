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

class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore
    val forecasts: MutableLiveData<ArrayList<ForecastModel>> = MutableLiveData()

    fun deleteForecast(documentId : String) {
        db.collection("forecasts").document(documentId).delete()
            .addOnSuccessListener { Log.d("tite", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("tite", "Error deleting document", e) }
    }


    fun getForecasts(classId: String): ListenerRegistration {
        val docRef = db.collection("forecasts").whereEqualTo("class_id", classId)
            .orderBy("meeting_time", Query.Direction.DESCENDING)

        return docRef.addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w("tite", "Listen failed", e)
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