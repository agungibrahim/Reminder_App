package com.example.reminder_app.network


import android.Manifest
import android.content.Context
import com.example.reminder_app.BuildConfig
import com.example.reminder_app.MainApp
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit


@Module
class NetworkModule(private val mainApp: MainApp) {

    @Provides
    @Singleton
    internal fun provideCall(): Retrofit {
        var latlong = "0,0"
//        val gpsTracker = GPSTracker()
        val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

//        if (EasyPermissions.hasPermissions(mainApp, permission.toString())) {
//            val location = gpsTracker.getLocation(mainApp)
//            if (location != null) {
//                latlong = location.latitude.toString() + "," + location.longitude
//            } else {
//                latlong = "0,0"
//            }
//        }

        val phonecode : String? = if(MainApp.instance.getIMEI != null) MainApp.instance.getIMEI else MainApp.instance.uniquePsuedoID

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Content-Type", "application/json")
//                    .addHeader("USER_AGENT", BuildConfig.APPNAME + "/" + BuildConfig.VERSION_NAME + " Android/" + Build.VERSION.SDK_INT)
//                    .addHeader("Authorization", "Bearer ${MainApp.instance.sharedPreferences?.getString("tokenUser", "token").toString()}")
////                    .addHeader("KNJDEVID", "" + MainApp.instance.sharedPreferences?.getString("imei",""))
//                    .addHeader("KNJLATLON", latlong)
//                    .addHeader("KNJSESSID", MainApp.instance.sharedPreferences?.getString("userid", "") + ";" + MainApp.instance.sharedPreferences?.getString("sessionid", ""))
//                    .addHeader("KNJLANGUAGE", "ID")
                    .build()
                val response = chain.proceed(request)
                response
            }.connectTimeout(120, TimeUnit.SECONDS)
//            .sslSocketFactory(GetSSLConfig.getSSLConfig(MainApp.instance.applicationContext).socketFactory)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(LoggingInterceptor())
            .build()


        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun providesNetworkService(
        retrofit: Retrofit
    ): NetworkService {
        return retrofit.create(NetworkService::class.java)
    }

    @Provides
    @Singleton
    fun providesService(
        networkService: NetworkService
    ): Service {
        return Service(networkService)
    }

    @Provides
    @Singleton
    @ForApplication
    internal fun provideApplicationContext(): Context {
        return mainApp
    }

}
