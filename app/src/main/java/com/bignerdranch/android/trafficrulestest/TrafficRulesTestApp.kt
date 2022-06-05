package com.bignerdranch.android.trafficrulestest

import android.app.Application
import com.bignerdranch.android.trafficrulestest.ui.tickets.TicketsRepository

class TrafficRulesTestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        TicketsRepository.initialize(this)
    }
}