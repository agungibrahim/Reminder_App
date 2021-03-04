package com.example.reminder_app.network

import android.os.StrictMode
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException

class LoggingInterceptor : Interceptor {

    private final val TAG = "Logging Interceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "LoggingInterceptor inside intercept callback");
        val request = chain.request()
        val t1 = System.nanoTime()
        var requestLog = String.format(
            "Sending request %s on %s%n%s",
            request.url(), chain.connection(), request.headers()
        )
        if (request.method().compareTo("post", ignoreCase = true) == 0) {
            requestLog = "\n" + requestLog + "\n" + bodyToString(request)
        }
        Log.d(TAG,"$TAG Request : $requestLog")
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        val responseLog = String.format(
            "Received response for %s in %.1fms%n%s",
            response.request().url(), (t2 - t1) / 1e6, response.headers()
        )

        val bodyString = response.body()!!.string()

        Log.d(TAG,"$TAG response only\n$bodyString")

        Log.d(TAG,"$TAG,$TAG response\n$responseLog\n$bodyString")


        return response.newBuilder()
            .body(ResponseBody.create(response.body()!!.contentType(), bodyString))
            .build()

    }

    companion object {

        fun bodyToString(request: Request): String {
            try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body()!!.writeTo(buffer)
                return buffer.readUtf8()
            } catch (e: IOException) {
                return "did not work"
            }

        }
    }
}