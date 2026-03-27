package com.salah.prayertimes.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class PrayerTimesWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val data = fetchPrayerTimes()
            ensureChannel()
            showTestNotification("Prayer times updated", "Fajr ${data.fajr} • Maghrib ${data.maghrib}")
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun fetchPrayerTimes(): PrayerTimesData {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())
        val apiKey = "cde641ff-cdde-4d25-8a62-4ec8cabc7f57"
        val url = "https://www.londonprayertimes.com/api/times/?format=json&key=$apiKey&date=$today&24hours=true"
        val response = URL(url).readText()
        val json = JSONObject(response)
        return PrayerTimesData(
            fajr = json.getString("fajr"),
            dhuhr = json.getString("dhuhr"),
            asr = json.getString("asr"),
            maghrib = json.getString("magrib"),
            isha = json.getString("isha")
        )
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val ch = NotificationChannel("prayer_times", "Prayer Times", NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(ch)
        }
    }

    private fun showTestNotification(title: String, text: String) {
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val n = NotificationCompat.Builder(applicationContext, "prayer_times")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        nm.notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), n)
    }
}

data class PrayerTimesData(
    val fajr: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)


