package com.example.reminder_app.network

import com.example.reminder_app.network.response.ResponseReminder
import retrofit2.http.*
import rx.Observable


interface NetworkService {

    @GET("5e7988f52d00005cbf18bd7b")
    fun getReminder(): Observable<ResponseReminder>

}
