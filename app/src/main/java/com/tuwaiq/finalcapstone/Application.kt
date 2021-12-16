package com.tuwaiq.finalcapstone

import android.app.Application
import com.tuwaiq.finalcapstone.repo.Repo

class Application: Application() {

    override fun onCreate() {
        super.onCreate()

        Repo.initialize(this)
    }
}