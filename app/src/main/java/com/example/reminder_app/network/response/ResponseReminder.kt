package com.example.reminder_app.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseReminder {

    @Expose
    @SerializedName("todos")
    var todos: List<Todos>? = null

    class Todos {
        @Expose
        @SerializedName("id")
        var id: Int? = null
        @Expose
        @SerializedName("title")
        var title: String? = null
        @Expose
        @SerializedName("description")
        var description: String? = null
        @Expose
        @SerializedName("dateTime")
        var dateTime: String? = null
    }
}