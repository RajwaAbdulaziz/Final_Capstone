package com.tuwaiq.finalcapstone

import android.app.Application
import androidx.hilt.work.HiltWorker
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.tuwaiq.finalcapstone.data.repo.Repo
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Application: Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        Repo.initialize(this)

    }
        @Inject
        lateinit var workerFactory: HiltWorkerFactory

        override fun getWorkManagerConfiguration() =
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
    }

