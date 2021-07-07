package com.sunnyside.kookoo.notification.api

import com.sunnyside.kookoo.notification.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("$BASE_URL/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: NotificationApi by lazy {
        retrofit.create(NotificationApi::class.java)
    }
}