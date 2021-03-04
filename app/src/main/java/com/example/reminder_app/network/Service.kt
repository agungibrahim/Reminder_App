package com.example.reminder_app.network

import com.example.reminder_app.network.response.ResponseReminder
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class Service(private val networkService: NetworkService) {

    fun getReminder(callback: callbackGetReminder): Subscription {
        return networkService.getReminder()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable -> Observable.error(throwable) }
            .subscribe(object : Subscriber<ResponseReminder>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(NetworkError(e).toString())
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: ResponseReminder?) {
                    if (t != null) {
                        callback.onSuccess(t)
                    }
                }

            })
    }

    interface BaseCallback {
        fun onError(message: String)
    }

    interface callbackGetReminder : BaseCallback {
        fun onSuccess(response: ResponseReminder)
    }

//    interface callbackGetKontak : BaseCallback {
//        fun onSuccess(response: ResponseKontak)
//    }

    fun convertStringtoRequestBody(data: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), data)
    }

}
