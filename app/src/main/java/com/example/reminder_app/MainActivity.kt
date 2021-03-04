package com.example.reminder_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reminder_app.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        if (intent.extras != null) {
            val descString = intent.getStringExtra("notifdesc").toString()
            text_desc.text = descString
        }
    }
}