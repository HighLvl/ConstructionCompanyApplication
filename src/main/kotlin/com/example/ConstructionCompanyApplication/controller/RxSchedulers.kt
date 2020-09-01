package com.example.ConstructionCompanyApplication.controller

import io.reactivex.Scheduler
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.schedulers.Schedulers

object RxSchedulers {
    val main: Scheduler get() = JavaFxScheduler.platform()
    val io: Scheduler get() = Schedulers.io()
    init {
        Schedulers.newThread()
    }
}