package com.example.reminder_app.pages.addreminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import com.example.reminder_app.MainApp
import com.example.reminder_app.R
import com.example.reminder_app.network.response.ResponseReminder
import com.example.reminder_app.utils.BaseActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.add_reminder_layout.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class AddReminderActivity : BaseActivity() {

    var formater = SimpleDateFormat("dd-MM-yyyy")
    var timeFormat = SimpleDateFormat("kk:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_reminder_layout)
        init()
    }

    fun init() {
        edt_title.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        edt_desc.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        edt_date_time.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val date = formater.format(selectedDate.time)
                timePicker(date)
            },
                now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        btn_submit.setOnClickListener {
            if (!edt_title.text.isNullOrEmpty() && !edt_desc.text.isNullOrEmpty() && !edt_date_time.text.isNullOrEmpty()) {
                if (!MainApp.instance.sharedPreferences?.getString("DataReminder", "").toString().isNullOrEmpty()) {
                    val gson = Gson()
                    val json : String = MainApp.instance.sharedPreferences?.getString("DataReminder", "").toString()
                    val type: Type = object : TypeToken<ArrayList<ResponseReminder.Todos>?>() {}.type
                    val listReminder : ArrayList<ResponseReminder.Todos> = gson.fromJson(json, type)

                    val randomInt = (0..20).random()
                    val model = ResponseReminder.Todos()
                    model.id = randomInt
                    model.title = edt_title.text.toString()
                    model.description = edt_desc.text.toString()
                    model.dateTime = edt_date_time.text.toString()

                    listReminder.add(model)
                    val jsonString = Gson().toJson(listReminder)
                    MainApp.instance.sharedPreferences?.edit()?.putString("DataReminder", jsonString)?.apply()
                    onBackPressed()
                }
            } else Toast.makeText(this, "Data belum lengkap", Toast.LENGTH_SHORT).show()
        }
    }

    fun timePicker(date : String) {
        val now = Calendar.getInstance()
        val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val selectedTime = Calendar.getInstance()
            selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
            selectedTime.set(Calendar.MINUTE,minute)
            edt_date_time.setText(date + " " + timeFormat.format(selectedTime.time))
        },
            now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),true)
        timePicker.show()
    }
}