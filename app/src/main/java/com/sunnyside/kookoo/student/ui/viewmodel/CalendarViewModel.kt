package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.CalendarAnnouncementModel
import com.sunnyside.kookoo.student.model.CalendarForecastModel

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore
    val events: MutableLiveData<List<Any>> = MutableLiveData()

    fun getEvents(classId: String, dateId: String) {
        val eventsRef = db.collection("events")
            .whereEqualTo("class_id", classId)
            .whereEqualTo("date", dateId)

        eventsRef.get()
            .addOnSuccessListener { documents ->
                if (documents != null && !documents.isEmpty) {
                    val document = documents.first()
                    val eventsResponse = document["events"] as List<*>
                    val eventsListResponse: MutableList<Any> = mutableListOf()

                    for (event in eventsResponse) {
                        event as Map<*, *>

                        val type = event["type"]

                        if (type == "forecast") {
                            val eventData = event["data"] as Map<*, *>
                            val newForecastEvent = CalendarForecastModel(
                                eventData["id"] as String,
                                eventData["title"] as String,
                                eventData["status"] as String
                            )
                            eventsListResponse.add(newForecastEvent)
                        } else if (type == "announcement") {
                            val eventData = event["data"] as Map<*, *>
                            val newAnnouncementEvent = CalendarAnnouncementModel(
                                eventData["id"] as String,
                                eventData["title"] as String
                            )
                            eventsListResponse.add(newAnnouncementEvent)
                        }
                    }

                    events.value = eventsListResponse
                    Log.d("tite", events.value.toString())
                } else {
                    Log.d("tite", "Current data: null")
                    events.value = emptyList()
                }
            }
    }


}