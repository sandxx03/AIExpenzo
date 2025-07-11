package com.example.aiexpenzo.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyReminder (context: Context){
    val delay = calculateDelayUntil(hour = 9)

    val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
        repeatInterval = 1,
        repeatIntervalTimeUnit = TimeUnit.DAYS
    )
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .addTag("daily_reminder")
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_expense_reminder",
        ExistingPeriodicWorkPolicy.REPLACE,
        workRequest
    )
}

fun cancelDailyReminder(context: Context){
    WorkManager.getInstance(context).cancelUniqueWork("daily_expense_reminder")
}
fun calculateDelayUntil(hour: Int): Long {
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(now)) add(Calendar.DAY_OF_YEAR, 1) // next day
    }
    return target.timeInMillis - now.timeInMillis
}