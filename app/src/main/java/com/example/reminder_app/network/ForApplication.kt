package com.example.reminder_app.network

import javax.inject.Qualifier
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class ForApplication
