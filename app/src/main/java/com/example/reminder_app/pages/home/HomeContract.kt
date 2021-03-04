package com.example.reminder_app.pages.home

import com.example.reminder_app.network.response.ResponseReminder
import com.example.reminder_app.utils.BasePresenter
import com.example.reminder_app.utils.BaseView

class HomeContract {
    interface View : BaseView<Presenter> {
        fun showLoading()
        fun hideLoading()
        fun onError(response: String)
        fun onSuccessGetReminder(response: ResponseReminder)
    }

    interface Presenter : BasePresenter {
        fun getReminder()
    }
}