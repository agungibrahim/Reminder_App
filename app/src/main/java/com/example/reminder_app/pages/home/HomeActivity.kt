package com.example.reminder_app.pages.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reminder_app.MainActivity
import com.example.reminder_app.MainApp
import com.example.reminder_app.R
import com.example.reminder_app.network.Service
import com.example.reminder_app.network.response.ResponseReminder
import com.example.reminder_app.pages.addreminder.AddReminderActivity
import com.example.reminder_app.utils.BaseActivity
import com.example.reminder_app.utils.ConnectionDetector
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_home_layout.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomeContract.View {

    @Inject
    lateinit var service: Service
    private var presenter: HomeContract.Presenter? = null
    lateinit var cd: ConnectionDetector
    private var mAdapter : AdapterHome? = null
    private var alarmNotificationManager: NotificationManager? = null
    var NOTIFICATION_CHANNEL_ID = "Reminder_App_ID"
    var NOTIFICATION_CHANNEL_NAME = "Reminder_App_Channel"
    private val NOTIFICATION_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_layout)
        init()
    }

    override fun onResume() {
        super.onResume()

        if (cd.isConnectingToInternet(this)) {
            if (MainApp.instance.sharedPreferences?.getString("DataReminder", "").toString().isNullOrEmpty()) {
                presenter?.getReminder()
            } else addList()
        } else {
            toast("Tidak ada koneksi internet")
            addList()
        }
    }

    fun init() {
        MainApp.instance.appComponent?.inject(this)
        presenter = HomePresenter(service, this)

        cd = ConnectionDetector()
        cd.isConnectingToInternet(this)

        mAdapter = AdapterHome(this, this)
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_home.layoutManager = layoutManager
        rv_home.adapter = mAdapter

        fab.setOnClickListener {
            val intent = Intent(this, AddReminderActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showLoading() {
        showDialog(true)
    }

    override fun hideLoading() {
        showDialog(false)
    }

    override fun onError(response: String) {
        showMessage(response)
    }

    override fun onSuccessGetReminder(response: ResponseReminder) {
        if (response.todos?.size != 0) {
            val jsonString = Gson().toJson(response.todos)
            Log.e("data reminder", "$jsonString")
            MainApp.instance.sharedPreferences?.edit()?.putString("DataReminder", jsonString)?.apply()
            addList()
        }
    }

    override fun setPresenter(presenter: HomeContract.Presenter?) {
        this.presenter = presenter
    }

    fun addList() {
        if (!MainApp.instance.sharedPreferences?.getString("DataReminder", "").toString().isNullOrEmpty()) {
            val gson = Gson()
            val json : String = MainApp.instance.sharedPreferences?.getString("DataReminder", "").toString()
            val type: Type = object : TypeToken<ArrayList<ResponseReminder.Todos>?>() {}.type
            val listReminder : ArrayList<ResponseReminder.Todos> = gson.fromJson(json, type)
            listReminder.sortBy { it.dateTime?.toLong()?.minus(System.currentTimeMillis()) }
            mAdapter?.updateList(listReminder)
            mAdapter?.notifyDataSetChanged()
        }
    }

    fun sendNotification(context: Context, title: String, desc: String, date: String) {
        val notif_title = title
        val notif_content = desc
        alarmNotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val newIntent = Intent(context, MainActivity::class.java)
        newIntent.putExtra("notifdesc", desc)
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            newIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance
            )
            alarmNotificationManager?.createNotificationChannel(mChannel)
        }

        val alamNotificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        alamNotificationBuilder.setContentTitle(notif_title)
        alamNotificationBuilder.setSmallIcon(android.R.drawable.ic_popup_reminder)
        if (date.toLong() >= System.currentTimeMillis()) {
            alamNotificationBuilder.setColor(resources.getColor(R.color.teal_700))
        } else alamNotificationBuilder.setColor(resources.getColor(R.color.red))
        alamNotificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        alamNotificationBuilder.setContentText(notif_content)
        alamNotificationBuilder.setAutoCancel(true)
        alamNotificationBuilder.setContentIntent(contentIntent)
        alarmNotificationManager?.notify(NOTIFICATION_ID, alamNotificationBuilder.build())
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun String.toLong(): Long{
        val formatter = SimpleDateFormat("dd-MM-yyyy kk:mm")
        formatter.isLenient = false
        val oldTime = this
        val oldDate: Date = formatter.parse(oldTime)
        val oldMillis = oldDate.time
        return oldMillis
    }
}