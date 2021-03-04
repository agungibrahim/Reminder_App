package com.example.reminder_app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Environment
import android.provider.Settings
import com.example.reminder_app.component.AppsComponent
import com.example.reminder_app.component.DaggerAppsComponent
import com.example.reminder_app.network.NetworkModule
import rx.android.BuildConfig
import timber.log.Timber
import java.io.*
import java.util.*


class MainApp : Application() {

    var appComponent: AppsComponent? = null
    var sharedPreferences: SharedPreferences? = null
        private set

    var isTaskOffline = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        appComponent = DaggerAppsComponent.builder().networkModule(NetworkModule(this)).build()
        appComponent?.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val dldFile = File(Environment.getExternalStorageDirectory(), "dbhelper.sqlite")

        isTaskOffline = false

    }

    val uniquePsuedoID: String
        get() {
            val m_szDevIDShort =
                "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10
            var serial: String? = null
            try {
                serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
                return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
            } catch (exception: Exception) {
                serial = "serial"
            }

            return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
        }

    val getIMEI: String?
        get() {
            return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        }

    fun onSessionExpired(context: Context) {
//        var intent = Intent(context, LoginActivity::class.java)
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
    }

    companion object {
        lateinit var instance: MainApp
    }
}