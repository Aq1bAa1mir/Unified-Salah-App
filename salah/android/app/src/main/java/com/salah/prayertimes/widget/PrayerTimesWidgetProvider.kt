package com.salah.prayertimes.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.action.actionStartBroadcastReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.unit.ColorProvider
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.work.*
import com.salah.prayertimes.MainActivity
import com.salah.prayertimes.service.PrayerTimesWorker
import java.util.concurrent.TimeUnit

class PrayerTimesWidgetProvider : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PrayerTimesWidget()
    
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Schedule periodic updates
        schedulePeriodicUpdates(context)
    }
    
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Cancel updates when all widgets are removed
        WorkManager.getInstance(context).cancelUniqueWork("prayer_times_updates")
    }
    
    private fun schedulePeriodicUpdates(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
            
        val updateRequest = PeriodicWorkRequestBuilder<PrayerTimesWorker>(
            15, TimeUnit.MINUTES // Update every 15 minutes
        )
            .setConstraints(constraints)
            .build()
            
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "prayer_times_updates",
                ExistingPeriodicWorkPolicy.KEEP,
                updateRequest
            )
    }
}

class PrayerTimesWidget : GlanceAppWidget() {
    override val stateDefinition = PrayerTimesWidgetStateDefinition
    
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            PrayerTimesWidgetContent()
        }
    }
}

@Composable
fun PrayerTimesWidgetContent() {
    val state = currentState<PrayerTimesWidgetState>()
    
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(android.graphics.Color.parseColor("#1c1c1c")))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Next Prayer",
            style = TextStyle(
                color = ColorProvider(android.graphics.Color.parseColor("#00bcd4")),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = GlanceModifier.padding(bottom = 4.dp)
        )
        
        Text(
            text = state.nextPrayerName,
            style = TextStyle(
                color = ColorProvider(android.graphics.Color.WHITE),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = GlanceModifier.padding(bottom = 4.dp)
        )
        
        Text(
            text = state.countdown,
            style = TextStyle(
                color = ColorProvider(android.graphics.Color.WHITE),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = GlanceModifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.SpaceBetween
        ) {
            Text(
                text = "Fajr: ${state.fajrTime}",
                style = TextStyle(
                    color = ColorProvider(android.graphics.Color.parseColor("#a0a0a0")),
                    fontSize = 10.sp
                )
            )
            Text(
                text = "Maghrib: ${state.maghribTime}",
                style = TextStyle(
                    color = ColorProvider(android.graphics.Color.parseColor("#a0a0a0")),
                    fontSize = 10.sp
                )
            )
        }
    }
}

data class PrayerTimesWidgetState(
    val nextPrayerName: String = "Loading...",
    val countdown: String = "--:--:--",
    val fajrTime: String = "--:--",
    val maghribTime: String = "--:--"
)

object PrayerTimesWidgetStateDefinition : GlanceStateDefinition<PrayerTimesWidgetState> {
    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<PrayerTimesWidgetState> {
        return PreferenceDataStoreFactory.create {
            PrayerTimesWidgetState()
        }
    }
}
