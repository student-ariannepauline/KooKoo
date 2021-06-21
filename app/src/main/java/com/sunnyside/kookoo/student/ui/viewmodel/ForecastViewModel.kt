package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.ForecastModel
import java.time.ZoneId

class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore
    val forecasts: MutableLiveData<List<ForecastModel>> = MutableLiveData()

    fun getForecasts(classId: String): ListenerRegistration {
        val docRef = db.collection("forecasts").whereEqualTo("class_id", classId)

        return docRef.addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w("tite", "Listen failed", e)
                return@addSnapshotListener
            }

            if (documents != null && !documents.isEmpty) {
                val forecastListResponse: MutableList<ForecastModel> = mutableListOf()

                for (document in documents) {
                    val title = document["title"] as String
                    val link = document["link"] as String
                    val status = document["status"] as String
                    val meetingTime = document["meeting_time"] as Timestamp

                    val meetingTimeLocalDateTime =
                        meetingTime.toDate().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDateTime()

                    val forecast = ForecastModel(
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