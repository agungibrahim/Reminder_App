package com.example.reminder_app.component

import com.example.reminder_app.MainActivity
import com.example.reminder_app.MainApp
import com.example.reminder_app.network.NetworkModule
import com.example.reminder_app.pages.home.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface AppsComponent {

    fun inject(mainApp: MainApp)
    fun inject(mainActivity: MainActivity)
    fun inject(homeActivity: HomeActivity)
}
