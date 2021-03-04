package com.example.reminder_app.pages.home

import com.example.reminder_app.network.Service
import com.example.reminder_app.network.response.ResponseReminder
import rx.subscriptions.CompositeSubscription

class HomePresenter(val service: Service, var view: HomeContract.View) : HomeContract.Presenter {

    private val subscriptions = CompositeSubscription()

    init {
        this.view.setPresenter(this)
    }

    override fun getReminder() {
        view.showLoading()
        val subscription = service.getReminder(object : Service.callbackGetReminder {
            override fun onSuccess(response: ResponseReminder) {
                view.hideLoading()
                view.onSuccessGetReminder(response)
            }

            override fun onError(message: String) {
                view.onError(message)
                view.hideLoading()
            }

        })

        subscriptions.add(subscription)
    }

    override fun start() {

    }

}