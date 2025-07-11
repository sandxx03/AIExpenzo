package com.example.aiexpenzo.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.aiexpenzo.R

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
): Worker(context, workerParams){

    override fun doWork(): Result {
        showReminderNotification()
        return Result.success()
    }

    private fun showReminderNotification(){
        val channelId = "expense_reminder_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,
                "Expense Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Don't forget to log your expenses!")
            .setContentText("Open AIExpenzo and track your spending for today.")
            .setSmallIcon(R.drawable.logo) // Replace with your icon
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
