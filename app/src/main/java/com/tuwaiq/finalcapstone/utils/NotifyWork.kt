package com.tuwaiq.finalcapstone.utils

import android.app.*
import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tuwaiq.finalcapstone.MainActivity
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.data.repo.MoodRepoImpl
import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import com.tuwaiq.finalcapstone.domain.use_cases.CheckMoodUseCase
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val CHANNEL_ID = "channell"
private const val CHANNEL_NAME = "channelname"
private const val NOTIFICATION_ID = 0
private const val TAG = "NotifyWork"

class NotifyWork (val context: Context, params: WorkerParameters): Worker(context, params) {
    @DelicateCoroutinesApi
    override fun doWork(): Result {
        Log.d(TAG, "HERE")

        val moodRepo: MoodRepo = MoodRepoImpl()
        val checkMoodUseCase = CheckMoodUseCase(moodRepo)

        var bool = false
        GlobalScope.launch {
            checkMoodUseCase().collect {
                if (it) {
                    bool = true
                }
                if (!bool) {
                    Log.d(TAG, "no mood")
                    val channel = NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    ).apply {
                        lightColor = Color.GREEN
                        enableLights(true)
                    }

                    val manager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    manager.createNotificationChannel(channel)
                    val intent = Intent(context, MainActivity::class.java)
                    val pendingIntent = TaskStackBuilder.create(context).run {
                        addNextIntentWithParentStack(intent)
                        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                    }

                    val notification = context.let {
                        NotificationCompat.Builder(it, CHANNEL_ID)
                    }

                        .setContentTitle("Log in Your mood for today!")
                        .setSmallIcon(R.drawable.good)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .build()

                    val notificationManager =
                        context.let { NotificationManagerCompat.from(it) }

                    notificationManager.notify(NOTIFICATION_ID, notification)

                } else {
                    Log.d(TAG, "yes mood")
                }
            }
        }
        return Result.success()
    }
}