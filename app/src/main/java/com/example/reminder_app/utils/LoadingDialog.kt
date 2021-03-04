package com.example.reminder_app.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.example.reminder_app.R
import kotlinx.android.synthetic.main.loading_dialog.*

class LoadingDialog(context: Context) : Dialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)
        progress_bar.isIndeterminate = true
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}