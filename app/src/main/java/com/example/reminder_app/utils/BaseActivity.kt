package com.example.reminder_app.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.reminder_app.MainApp
import com.example.reminder_app.R

open class BaseActivity : AppCompatActivity() {
    lateinit var fm: FragmentManager
    internal var pd: LoadingDialog? = null
    internal var pd1: ProgressDialog? = null
    lateinit var builder: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fm = supportFragmentManager
        pd = LoadingDialog(this!!)
        pd1 = ProgressDialog(this, R.style.MyAlertDialogStyle)
        pd1!!.setMessage("Loading ...")


    }

    protected fun loadFragment(fragment: Fragment, baseframe: View) {
        val ft = fm.beginTransaction()
        ft.replace(baseframe.id, fragment)
        ft.commit()
    }

    protected fun replaceFragment(fragment: Fragment, baseframe: View) {
        val ft = fm.beginTransaction()
        ft.replace(baseframe.id, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    protected fun replaceRemoveFragment(fragment: Fragment, baseframe: View) {
        fm.popBackStack()
        val ft = fm.beginTransaction()
        ft.replace(baseframe.id, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    fun showDialog(show: Boolean) {
        if (null != pd)
            if (show)
                pd!!.show()
            else
                pd!!.dismiss()
    }

    fun showLoading(show: Boolean) {
        if (null != pd1)
            if (show)
                pd1!!.show()
            else
                pd1!!.dismiss()
    }

    fun showMessage(message: String?) {
        if(applicationContext != null)
            builder.setMessage(message)
                .setTitle(MainApp.instance.getString(R.string.app_name))
                .setPositiveButton("OK") { dialogInterface, i -> dialogInterface.dismiss() }
                .show()
    }
}
